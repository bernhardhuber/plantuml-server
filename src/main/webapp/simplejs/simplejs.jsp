<%--
    Document   : simplejs
    Created on : Sep 26, 2021, 6:44:58 AM
    Author     : berni3
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="contextroot" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
    <head>
        <title>SimpleJs Example</title>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <%--
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
        --%>
        <link rel="stylesheet" href="${contextroot}/webjars/bootstrap/3.4.1/css/bootstrap.min.css">
        <script src="${contextroot}/webjars/jquery/3.5.1/jquery.min.js"></script>
        <script src="${contextroot}/webjars/bootstrap/3.4.1/js/bootstrap.min.js"></script>

        <link rel="stylesheet" href="${contextroot}/webjars/codemirror/3.21/lib/codemirror.css" />
        <script src="${contextroot}/webjars/codemirror/3.21/lib/codemirror.js"></script>
        <script>
            var myCodeMirror;
            window.onload = function () {
                myCodeMirror = CodeMirror.fromTextArea(
                        document.getElementById("umltext"),
                        {
                            lineNumbers: true
                        }
                );
            };
        </script>

        <script src="${contextroot}/resource/simplejs/encode64.js"></script>
        <script src="${contextroot}/resource/zlib/rawdeflate.js"></script>
        <script src="${contextroot}/resource/zlib/rawinflate.js"></script>

        <script>
            var compress = (function () {
                var umlDefault = "@startuml\n"
                        + "Alice -> Bob : Hello\n"
                        + "@enduml";
                var imgPngUrl = "${contextroot}" + "/png/";
                var imgSvgUrl = "${contextroot}" + "/svg/";
                
                var umlTextId = 'umltext';
                var umlImageId = 'umlimage';

                return function compress() {
                    // save codeMirror-text in textArea
                    myCodeMirror.save();
                    
                    var textAreaElement = document.getElementById(umlTextId);
                    var s = textAreaElement.value

                    //UTF8
                    var sAsUnescaped = unescape(encodeURIComponent(s));
                    var sEncoded = encode64(deflate(sAsUnescaped, 9));
                    var imgUrl = imgPngUrl;
                    var imgElement = document.getElementById(umlImageId);
                    imgElement.src = imgUrl + sEncoded;
                };
            })();
        </script>
    </head>
    <body>
        <div class="jumbotron text-center">
            <h1>PlantUml</h1>
            <p>PlantUml diagram service</p>
            <p>
                <a href="${contextroot}/">Plant UML Home</a>
                | <a href="${contextroot}/bootstrap/bootstrap1.jsp">Bootstrap</a>
                | <a href="${contextroot}/simplejs/simplejs.jsp">SimpleJs</a>
            </p>
        </div>

        <div class="container-fluid">
            <div class="col-md-12">
                <h3>UML-Text</h3>
                <div>
                    <%-- CONTENT --%>
                    <%--form method="post" accept-charset="UTF-8"  action="${contextroot}/encodeform"--%>
                    <div class="form-group">
                        <label id="umltextLabel" for="umltext">UML</label>
                        <textarea id="umltext" name="text" class="form-control"
                                  autofocus="true"
                                  cols="80"
                                  rows="20"></textarea>
                    </div>
                    <button id="umltextSubmit" type="submit" class="btn btn-primary"
                            onclick="compress();">Submit</button>

                    <%--/form --%>
                </div>
                <h3>Error Messages</h3>
                <div>
                    <p id="errorText">

                    </p>
                </div>

                <h3>Diagram</h3>
                <div id="diagram" class="thumbnail">
                    <img id="umlimage" alt="PlantUML diagram" >
                    <div class="caption">
                        <p id="encoded">Encoded diagram<br/>Lorem ipsum...</p>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
