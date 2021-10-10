<%--
    Document   : newjsp
    Created on : Dec 10, 2020, 8:43:38 PM
    Author     : berni3
--%>
<%@page contentType="text/html" pageEncoding="UTF-8" session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="cfg" value="${applicationScope['cfg']}" />
<c:set var="contextroot" value="${pageContext.request.contextPath}" />
<c:if test="${(pageContext.request.scheme == 'http' && pageContext.request.serverPort != 80) ||
              (pageContext.request.scheme == 'https' && pageContext.request.serverPort != 443) }">
    <c:set var="port" value=":${pageContext.request.serverPort}" />
</c:if>
<c:set var="scheme" value="${(not empty header['x-forwarded-proto']) ? header['x-forwarded-proto'] : pageContext.request.scheme}" />
<c:set var="hostpath" value="${scheme}://${pageContext.request.serverName}${port}${contextroot}" />
<c:if test="${!empty encoded}">
    <c:set var="imgurl" value="${hostpath}/xxx-generate-image/${encoded}" />
    <c:set var="svgurl" value="${hostpath}/xxx-generate-image/SVG/${encoded}" />
    <c:set var="txturl" value="${hostpath}/xxx-generate-image/ATXT/${encoded}" />

    <c:set var="epsurl" value="${hostpath}/xxx-generate-image/EPS/${encoded}" />
    <c:set var="epstextturl" value="${hostpath}/xxx-generate-image/EPS_TEXT/${encoded}" />

    <c:if test="${!empty mapneeded}">
        <c:set var="mapurl" value="${hostpath}/xxx-generate-image/MAP/${encoded}" />
    </c:if>
</c:if>

<!DOCTYPE html>
<html lang="en">
    <head>
        <title>Bootstrap Example</title>
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
            window.onload = function () {
                var myCodeMirror = CodeMirror.fromTextArea(
                        document.getElementById("umltext"),
                        {
                            lineNumbers: true
                        }
                );
            };
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
            <div class="row">
                <div class="col-md-2">
                    <%-- snippet --%>
                    <h3>Snippets</h3>
                    <ol >
                        <c:forEach items="${snippetEntryList}" var="_snippetEntry">
                            <li title="${_snippetEntry.createdWhen}">
                                <samp title="${_snippetEntry.description}"><c:out value="${_snippetEntry.text}"/></samp>
                            </li>
                        </c:forEach>
                    </ol>

                    <%-- example --%>
                    <h3>Examples</h3>
                    <ol >
                        <c:forEach items="${exampleEntryList}" var="_exampleEntry">
                            <li title="${_exampleEntry.createdWhen}">
                                <pre><c:out value="${_exampleEntry.decoded}"/></pre>
                            </li>
                        </c:forEach>
                    </ol>
                    <p>Row1 Col1 Lorem ipsum dolor sit amet, consectetur adipisicing elit...</p>
                </div>
                <div class="col-md-9">
                    <h3>UML-Text</h3>
                    <div>
                        <%-- CONTENT --%>
                        <form method="post" accept-charset="UTF-8"  action="${contextroot}/encodeform">
                            <div class="form-group">
                                <label id="umltextLabel" for="umltext">UML</label>
                                <textarea id="umltext" name="text" class="form-control"
                                          autofocus="true"
                                          cols="80"
                                          rows="20"><c:out value="${decoded}"/></textarea>
                            </div>
                            <button id="umltextSubmit" type="submit" class="btn btn-primary">Submit</button>
                        </form>
                    </div>

                    <h3>Diagram</h3>
                    <c:if test="${!empty imgurl}">
                        <div id="diagram" class="thumbnail">
                            <img src="${imgurl}" alt="PlantUML diagram" >
                            <div class="caption">
                                <p>Encoded diagram : ${encoded}<br/>Lorem ipsum...</p>
                            </div>
                        </div>

                        <h3>Alternate Formats</h3>
                        <div>
                            <p>
                                Choose from some alternate format:
                            </p>
                            <div class="btn-group">
                                <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
                                    File Format<span class="caret"></span>
                                </button>
                                <ul class="dropdown-menu" role="menu">
                                    <li><a href="${svgurl}" >View as SVG</a>
                                    <li><a href="${txturl}" >View as ASCII Art</a></li>
                                    <li><a href="${epsurl}" >View as EPS</a></li>
                                    <li><a href="${epstextturl}" >View as EPS Text</a></li>
                                    <li><a href="${hostpath}/MAP/${encoded}" >View as HTML Map</a></li>

                                    <c:if test="${!empty mapurl}">
                                        <li><a href="${mapurl}" >View Map Data</a></li>
                                        </c:if>
                                </ul>
                            </div>
                            <c:if test="${cfg['SHOW_SOCIAL_BUTTONS'] == 'on' }">
                                <%--@ include file="resource/socialbuttons2.jspf" --%>
                            </c:if>
                        </div>

                        <div>
                            <p>
                                Choose more alternate formats:
                            </p>
                            <form method="POST" action="${hostpath}/xxx-generate-image">
                                <input type="hidden" name="encoded" value="${encoded}"/>
                                <div class="form-group">
                                    <label for="fileFormat">File Format</label>

                                    <select id="fileFormat" name="fileFormat" class="form-control">
                                        <option label="PNG" value="PNG"/>
                                        <option label="SVG" value="SVG"/>
                                        <option label="EPS" value="EPS"/>
                                        <option label="EPS_TEXT" value="EPS_TEXT"/>
                                        <option label="ATXT" value="ATXT"/>
                                        <option label="UTXT" value="UTXT"/>
                                        <option label="XMI_STANDARD" value="XMI_STANDARD"/>
                                        <option label="XMI_STAR" value="XMI_STAR"/>
                                        <option label="XMI_ARGO" value="XMI_ARGO"/>
                                        <option label="SCXML" value="SCXML"/>
                                        <option label="PDF" value="PDF"/>
                                        <option label="MJPEG" value="MJPEG"/>
                                        <option label="ANIMATED_GIF" value="ANIMATED_GIF"/>
                                        <option label="HTML" value="HTML"/>
                                        <option label="HTML5" value="HTML5"/>
                                        <option label="VDX" value="VDX"/>
                                        <option label="LATEX" value="LATEX"/>
                                        <option label="LATEX_NO_PREAMBLE" value="LATEX_NO_PREAMBLE"/>
                                        <option label="BASE64" value="BASE64"/>
                                        <option label="BRAILLE_PNG" value="BRAILLE_PNG"/>
                                        <option label="PREPROC" value="PREPROC"/>
                                    </select>
                                </div>
                                <button type="submit" class="btn btn-default">Submit</button>
                            </form>
                        </div>
                    </c:if>

                    <h3>History</h3>

                    <button class="btn btn-default" type="button"
                            data-toggle="collapse"
                            data-target="#collapseHistory"
                            aria-expanded="false"
                            aria-controls="collapseExample">
                        Toogle History
                    </button>

                    <div class="collapse" id="collapseHistory">
                        <div class="well">
                            <ol>
                                <c:forEach items="${historyEntryList}" var="_historyEntry">
                                    <li>
                                        <ul class="list-unstyled">
                                            <li><c:out value="${_historyEntry.createdWhen}"/></li>
                                            <li><samp><c:out value="${_historyEntry.encoded}"/></samp></li>
                                            <li><pre><c:out value="${_historyEntry.decoded}"/></pre></li>
                                </ul>
                            </li>
                                </c:forEach>
                        </ol>
                        </div>
                    </div>
                    <p>Row1 Column 2 Lorem ipsum dolor sit amet, consectetur adipisicing elit...</p>
                </div>
                <div class="col-md-1">
                    <h3>Row1 Column 3</h3>
                    <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit...</p>
                    <p>Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris...</p>
                </div>
            </div>

            <div class="row">
                <div class="col-md-12">
                    <h3>Row2 Column 1-3</h3>
                    <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit...</p>
                    <p>Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris...</p>
                </div>
            </div>
        </div>
    </body>
