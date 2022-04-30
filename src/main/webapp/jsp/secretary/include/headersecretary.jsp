<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<nav class="navbar navbar-expand-lg navbar-light bg-light">
    <a class="navbar-brand" href="#">Navbar</a>
    <button
            class="navbar-toggler"
            type="button"
            data-toggle="collapse"
            data-target="#navbarSupportedContent"
            aria-controls="navbarSupportedContent"
            aria-expanded="false"
            aria-label="Toggle navigation"
    >
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item p-2 active">
                <a href="<c:url value="/"/>">Home</a>
            </li>
            <li class="nav-item p-2">
                <a href="<c:url value="/secretary"/>">My Profile</a>
            </li>
            <li class="nav-item p-2">
                <a href="<c:url value="/secretary/addcourses"/>">Add Courses</a>
            </li>
            <li class="nav-item p-2">
                <a href="<c:url value="/secretary/addaccount"/>">Add Account</a>
            </li>
            <li class="nav-item p-2">
                <a href="<c:url value="/secretary/manageroles"/>">Manage Roles</a>
            </li>
            <li class="nav-item p-2">
                <a href="<c:url value="/secretary/addMedicalCertificate"/>">Add Medical Certificates</a>
            </li>
            <li class="nav-item p-2">
                <a href="<c:url value="/secretary/managessubscription"/>">Manage Subscription</a>
            </li>
        </ul>
        <!-- Right dropdown of the navbar -->
        <!-- Right dropdown of the navbar -->
        <ul class="navbar-nav ml-auto">
            <li class="nav-item dropdown">
                <a class="nav-link dropdown-toggle" id="navbarRightDropdown" role="button" data-toggle="dropdown"
                   aria-haspopup="true" aria-expanded="false">
                    <!-- Load user avatar if any -->
                    <c:choose>
                        <c:when test="${null != sessionScope.avatarPath}">
                            <img class="rounded-circle" src="<c:url value="/avatar"/>" width="36px" height="36px">
                        </c:when>
                        <c:otherwise>
                            <i class="fa-solid fa-user p-2"></i>
                        </c:otherwise>
                    </c:choose>
                </a>
                <div class="dropdown-menu dropdown-menu-right text-right" aria-labelledby="navbarRightDropdown">
                    <!-- Load options -->
                    <c:choose>
                        <c:when test="${empty sessionScope.roles}">
                            <a class="dropdown-item" href="<c:url value="/register"/>">Register</a>
                            <a class="dropdown-item" href="<c:url value="/login"/>">Login</a>
                        </c:when>
                        <c:otherwise>
                            <a class="dropdown-item disabled"><c:out value="${sessionScope.email}"/></a>
                            <div class="dropdown-divider"></div>
                            <c:forEach var="role" items="${sessionScope.roles}">
                                <a class="dropdown-item" href="<c:url value="/${role}"/>"><c:out value="${role}"/></a>
                            </c:forEach>
                            <a class="dropdown-item" href="<c:url value="/logout"/>">Logout</a>
                        </c:otherwise>
                    </c:choose>
                </div>
            </li>
        </ul>
    </div>
</nav>














