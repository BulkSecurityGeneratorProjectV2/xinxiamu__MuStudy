<!DOCTYPE html>  
    <html>  
    <head>  
    <meta charset="utf-8">  
        <script src="http://code.jquery.com/jquery-2.1.3.min.js"></script>  
    <script>  
    $(document).ready(function(){  
          
          
    $("#but1").click(function(){  
         $.ajax({  
            url:'http://127.0.0.1:8082/get',  
            type: "get",  
            async: false,  
            dataType: "jsonp",  
            jsonp: "callbackparam", //服务端用于接收callback调用的function名的参数   
            jsonpCallback: "success_jsonpCallback", //callback的function名称,服务端会把名称和data一起传递回来   
            success: function(json) {  
             alert(json.name);  
            },  
            error: function(){alert('Error');}  
    });  
    });  
      
      
    $("#but2").click(function(){  
         $.ajax({  
            url:'http://127.0.0.1:8082/getJsonp',  
            type: "get",  
            async: false,  
            dataType: "jsonp",  
            jsonp: "callbackparam", //服务端用于接收callback调用的function名的参数   
            jsonpCallback: "success_jsonpCallback", //callback的function名称,服务端会把名称和data一起传递回来   
            success: function(json) {  
             alert(json.name);    
            },  
            error: function(){alert('Error');}  
    });  
    });  
      
      
    });  
    </script>  
    </head>  
    <body>  
      
    <div id="div1"><h2>使用 jQuery AJAX 来改变文本</h2></div>  
    <button id="but1">按钮1</button> <br/>  
    <button id="but2">按钮2</button>  
      
    </body>  
    </html>  