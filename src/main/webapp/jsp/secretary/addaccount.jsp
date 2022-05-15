<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <title>Add Account</title>
        <meta charset="UTF-8">
        <jsp:include page="/jsp/include/style.jsp"/>
        <link rel="stylesheet" href="<c:url value="/css/main.css"/>">
        <jsp:include page="/jsp/include/favicon.jsp"/>
    </head>
    <body>
        <header>
            <jsp:include page="/jsp/secretary/include/headersecretary.jsp"/>
        </header>
        <main class="global-container">
            <jsp:include page="/jsp/include/message.jsp"/>
            <form method="post" action="<c:url value="/secretary/addaccount"/>" enctype="multipart/form-data" id="form">
                <div class="form-group row">
                    <label for="tax_code" class="col-sm-3 col-form-label">Tax Code :</label>
                    <div class="col-sm-9">
                        <input type="text" name="tax_code" id="tax_code" minlength="16" maxlength="16" class="form-control"
                               placeholder="Insert Tax Code" required/>
                    </div>
                </div>
        
                <!-- pattern="^[a-zA-Z]{6}[0-9]{2}[a-zA-Z][0-9]{2}[a-zA-Z][0-9]{3}[a-zA-Z]$" required-->
                <div class="form-group row">
                    <label for="first_name" class="col-sm-3 col-form-label">First Name :</label>
                    <div class="col-sm-9">
                        <input type="text" name="first_name" id="first_name" maxlength="30" class="form-control"
                               placeholder="Insert First Name" required/>
                    </div>
                </div>
        
                <div class="form-group row">
                    <label for="last_name" class="col-sm-3 col-form-label">Last Name :</label>
                    <div class="col-sm-9">
                        <input type="text" name="last_name" id="last_name" maxlength="30" class="form-control"
                               placeholder="Insert Last Name" required/>
                    </div>
                </div>
        
                <div class="form-group row">
                    <label for="birth_date" class="col-sm-3 col-form-label">Birth Date :</label>
                    <div class="col-sm-9">
                        <input type="date" name="birth_date" id="birth_date" class="form-control" required/>
                    </div>
                </div>
        
                <div class="form-group row">
                    <label for="address" class="col-sm-3 col-form-label">Address :</label>
                    <div class="col-sm-9">
                        <input type="text" name="address" id="address" class="form-control" placeholder="Insert Address"
                               required/>
                    </div>
                </div>
        
                <div class="form-group row">
                    <label for="telephone_number" class="col-sm-3 col-form-label">Telephone Number :</label>
                    <div class="col-sm-9">
                        <input type="tel" name="telephone_number" id="telephone_number" class="form-control"
                               placeholder="Insert Telephone Number" required/>
                    </div>
                </div>
        
                <div class="form-group row">
                    <label for="file" class="col-sm-3 col-form-label">Avatar (Optional) :</label>
                    <div class="col-sm-9">
                        <div class="custom-file">
                            <input type="file" name="avatar" id="file" class="custom-file-input"/>
                            <label class="custom-file-label" for="file">Choose File</label>
                        </div>
                    </div>
                </div>
        
                <div class="form-group row">
                    <label for="email" class="col-sm-3 col-form-label">Email :</label>
                    <div class="col-sm-9">
                        <input type="email" name="email" id="email" maxlength="40" class="form-control"
                               placeholder="Enter Email" required/>
                    </div>
                </div>
        
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">Role : </label>
                    <div class="col-sm-3">
                        <input type="checkbox" id="trainee" name="trainee"/>
                        <label for="trainee">Trainee</label>
                    </div>
                    <div class="col-sm-3">
                        <input type="checkbox" name="trainer" id="trainer"/>
                        <label for="trainer">Trainer</label>
                    </div>
                    <div class="col-sm-3">
                        <input type="checkbox" name="secretary" id="secretary"/>
                        <label for="secretary">Secretary</label>
                    </div>
                </div>
        
                <button type="submit" class="btn btn-outline-primary btn-lg" id="btn-register">Register</button>
            </form>
        
        </main>
        
        <jsp:include page="../include/footer.jsp"/>
        <jsp:include page="/jsp/include/scripts.jsp"/>
        <jsp:include page="/jsp/include/moment/scripts.jsp"/>
        <!-- Common JS to handle messages -->
        <script src="<c:url value="/js/message.js"/>"></script>
        <script src="<c:url value="/js/form-validation-functions.js"/>"></script>
        <script src="<c:url value="/js/file_upload.js"/>"></script>
        <script src="<c:url value="/js/register.js"/>"></script>
    </body>
</html>
