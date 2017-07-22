<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<sec:authentication var="principal" property="principal" />
<%@ page isELIgnored="false" %> 


<html>
<body>
<h2>Hello spring-OAuth2-JWT-hibernate-sso!</h2>
<h3>${principal.username}</h3>

<sec:authorize access="hasRole('ROLE_ADMIN')">
<p>
<a href="http://localhost:8080/spring-OAuth2-JWT-hibernate-resource/resources/admin?access_token=${cookie['access_token'].value}" class="btn btn-default btn-flat">Admin Page</a>
</p>
</sec:authorize>
<sec:authorize access="hasRole('ROLE_USER')">
<p>
<a href="http://localhost:8080/spring-OAuth2-JWT-hibernate-resource/resources/user?access_token=${cookie['access_token'].value}" class="btn btn-default btn-flat">User Page</a>
</p>
</sec:authorize>

<a href="<c:url value="/logout" />" class="btn btn-default btn-flat">Log out</a>
</body>
</html>
