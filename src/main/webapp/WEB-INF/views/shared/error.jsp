<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title><s:message code="shared.error.occurred"/></title>
    <style>
        body { font-family: Arial, sans-serif; background-color: #f8f9fa; display: flex; justify-content: center; align-items: center; height: 100vh; margin: 0; }
        .error-container { background: white; padding: 40px; border-radius: 8px; box-shadow: 0 4px 12px rgba(0,0,0,0.1); text-align: center; max-width: 500px; }
        .error-container h1 { color: #dc3545; font-size: 80px; margin: 0; }
        .error-container h3 { color: #343a40; }
        .error-container p { color: #6c757d; margin-bottom: 20px; line-height: 1.5; }
        .btn-home { display: inline-block; padding: 10px 20px; background-color: #0d6efd; color: white; text-decoration: none; border-radius: 4px; transition: 0.3s; }
        .btn-home:hover { background-color: #0b5ed7; }
    </style>
</head>
<body>
    <div class="error-container">
        <h1>Oops!</h1>
        <h3><s:message code="shared.error.has.occurred"/></h3>
        <p>${errorMsg}</p>
        <a href="${pageContext.request.contextPath}/" class="btn-home"><s:message code="shared.error.back.home"/></a>
    </div>
</body>
</html>
