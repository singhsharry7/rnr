<%@page import="com.snapdeal.reviews.cache.CacheResultWrapper"%>
<%@page import="com.snapdeal.reviews.cache.ObjectCache"%>
<%@page import="com.snapdeal.reviews.commons.Constants"%>
<%@page import="com.snapdeal.reviews.server.cache.CacheManager"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="com.snapdeal.reviews.service.AdministrationService"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.web.context.support.XmlWebApplicationContext"%>
<%@page autoFlush="true" %>
<% 
	String method = request.getParameter("method");
	ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(request.getServletContext());
	AdministrationService administrationService = applicationContext.getBean(AdministrationService.class);
	CacheManager cacheManager = applicationContext.getBean(CacheManager.class);
	ObjectCache<String, String> appPropertyCache = cacheManager.getAppPropertiesCache();

	if("start".equals(method)) {
		administrationService.triggerCacheWarmup();
	} else if("getStatus".equals(method)) {
		String cachedProducts = null;
		String totalProducts = null;
		
		CacheResultWrapper<String> value = appPropertyCache.get(Constants.NUMBER_OF_HOT_PRODUCTS);
		if(null != value){
			cachedProducts = value.getValue();
		}
		
		value = appPropertyCache.get(Constants.TOTAL_NUMBER_OF_PRODUCTS);
		if(null != value){
			totalProducts = value.getValue();
		}

		StringBuilder responseBuilder = new StringBuilder(100);
		responseBuilder.append("{");		
		responseBuilder.append("\"cachedProducts\":");
		responseBuilder.append(cachedProducts);
		responseBuilder.append(",");
		responseBuilder.append("\"totalProducts\":");
		responseBuilder.append(totalProducts);
		responseBuilder.append("}");
		response.getWriter().append(responseBuilder);
	} else { 
%>
<!doctype html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <title>Warm Up Cache</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
		<script src="warm-up/progress-bar.js"></script>
		<script src="warm-up/jquery.js"></script>
        <style>
            .progress {
                height: 300px;
            }
            .progress > svg {
                height: 100%;
                display: block;
            }
			.progressbar-text{
				font-size: 50px;
			}
			td { 
			    padding-left: 10px;
			    padding-top: 10px;
			}
			table { 
			    border-collapse: separate;
			}
        </style>
    </head>
    <body>
    	<header>
    		
    	</header>
        
        <div class="example-container" id="progress" style="width:30%;height:30%; margin: auto;"></div>
        <div style="margin:auto;width:200px;"><input type="button" id="warmUp" value="Warmup Cache" style="height:50px;width:200px;"/></div>
        <div style="margin:auto;width:400px;display:none;margin-top:15px;" id="cachesStats">
        	<table>
        		<tr>
        			<td style="white-space:nowrap;width:200px;"><b>Total Products: </b></td>
        			<td style="white-space:nowrap;width:200px;"></td>
        			<td style="white-space:nowrap;width:200px;"><b>Time Taken: </b></td>
        			<td style="white-space:nowrap;width:200px;"></td>
        		</tr>
        		<tr>
        			<td style="white-space:nowrap;width:200px;"><b>Cached Products: </b></td>
        			<td style="white-space:nowrap;width:200px;"></td>
					<td style="white-space:nowrap;width:200px;"><b>Time Left: </b></td>
        			<td style="white-space:nowrap;width:200px;"></td>        			
        		</tr>
        	</table>
        </div>	
        <footer>
        	
        </footer>
    </body>
    <script type="text/javascript">
	    String.prototype.trim = function(){  
	        return this.replace(/^\s+|\s+$/g,'');  
	    };  
    
    	(function(){
    		var cachesStats = $("#cachesStats");
    		
    		var startTime = null;
    		
    		var timer = null;
    		
    		var lastCachedCount = 0;
    		
    		var timeLimit = 5;
    		
            var circle = new ProgressBar.Circle('#progress', {
                color: '#FCB03C',
                strokeWidth: 6,
                trailWidth: 3,
                text: {value: '0%'},
            });
            
            function formatTime(time){
            	time = time/1000;
            	time = time.toFixed(0);
            	return (time/60).toFixed(0) + "m " + (time%60).toFixed(0) + "s";
            }
            
            function updateStatus (){
            	$.ajax({
            		url: "warm-up?method=getStatus", 
            		type: "get",
            		success: function(data){
            			data = data.trim();
            			data = JSON.parse(data);
            			$($(cachesStats.find("table").find("tr")[0]).find("td")[1]).html(data.totalProducts);
            			$($(cachesStats.find("table").find("tr")[1]).find("td")[1]).html(data.cachedProducts);
            			if (data.totalProducts == data.cachedProducts) {
            				window.clearInterval(timer);
            				timer = null;
            			}
            			var timeTook = new Date().getTime() - startTime;
            			$($(cachesStats.find("table").find("tr")[0]).find("td")[3]).html(formatTime(timeTook));
            			
            			// calculating remaining time
            			// show - if cahced Items is 0 else calculate and then show the time
            			if (parseInt(data.cachedProducts, 10) > 0) {
            				var itemsCached = data.cachedProducts - lastCachedCount;
            				if (itemsCached > 0) {
	            				var timeForCachingOneItem = timeLimit/itemsCached;
	            				var estimated = (timeForCachingOneItem * (data.totalProducts - data.cachedProducts) * 1000);
	            				lastCachedCount = data.cachedProducts;
	            				$($(cachesStats.find("table").find("tr")[1]).find("td")[3]).html(formatTime(estimated));
            				}
            			} else {
            				$($(cachesStats.find("table").find("tr")[1]).find("td")[3]).html("-");
            			}
            			// showing progress in circle if total produc count is not fetched don'y change the circle percent
            			if (parseInt(data.totalProducts, 10) > 0) {
            				circle.setText(((data.cachedProducts/data.totalProducts) * 100).toFixed(0)+" %");
            			}
            			circle.animate(data.cachedProducts/data.totalProducts);                  	
                	},
                	error: function(x,s,e) {
                		console.log(e);
                	}
            	});
    		}    
    	    $("#warmUp").click(function(){
    	    	$("#warmUp").attr("disabled", "disabled");
    	    	cachesStats.show();
    	    	$.ajax({
            		url: "warm-up?method=start"
            	});
    	    	startTime = new Date().getTime();
    	    	timer = window.setInterval(function() {updateStatus()}, timeLimit * 1000);
    	    });
    		
    	})();
    </script>
</html>
<%}%>