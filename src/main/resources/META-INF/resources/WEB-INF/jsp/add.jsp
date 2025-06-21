<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
    <title>Add Fixed Deposit</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-4">
    <div class="row mb-3">
        <div class="col">
            <h2>${fixedDeposit.id != null ? 'Edit' : 'Add'} Fixed Deposit</h2>
            <div class="float-end">
                <span class="badge bg-secondary">Logged in as: <sec:authentication property="name"/></span>
                <a href="/logout" class="btn btn-outline-danger btn-sm ms-2">Logout</a>
            </div>
        </div>
    </div>
    
    <!-- Navigation Tabs -->
    <ul class="nav nav-tabs mb-4">
        <li class="nav-item">
            <a class="nav-link" href="/">Dashboard</a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="/records">Records</a>
        </li>
        <li class="nav-item">
            <a class="nav-link active" href="/add">Add New FD</a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="/report">Generate Report</a>
        </li>
    </ul>
    
    <form:form action="/save" method="post" modelAttribute="fixedDeposit" cssClass="row g-3">
        <form:hidden path="id"/>
        <div class="col-md-6">
            <label for="personName" class="form-label">Person Name</label>
            <form:input path="personName" id="personName" cssClass="form-control" required="true"/>
        </div>
        <div class="col-md-6">
            <label for="bankName" class="form-label">Bank Name</label>
            <form:input path="bankName" id="bankName" cssClass="form-control" required="true"/>
        </div>
        <div class="col-md-6">
            <label for="accountNumber" class="form-label">Account Number</label>
            <form:input path="accountNumber" id="accountNumber" cssClass="form-control" required="true"/>
        </div>
        <div class="col-md-6">
            <label for="fromDate" class="form-label">From Date</label>
            <form:input path="fromDate" type="date" id="fromDate" cssClass="form-control" required="true"/>
        </div>
        <div class="col-md-6">
            <label for="toDate" class="form-label">To Date</label>
            <form:input path="toDate" type="date" id="toDate" cssClass="form-control" required="true"/>
        </div>
        <div class="col-md-6">
            <label for="investedAmount" class="form-label">Invested Amount</label>
            <form:input path="investedAmount" type="number" step="0.01" id="investedAmount" cssClass="form-control" required="true"/>
        </div>
        <div class="col-md-6">
            <label for="returnAmount" class="form-label">Return Amount</label>
            <form:input path="returnAmount" type="number" step="0.01" id="returnAmount" cssClass="form-control" required="true"/>
        </div>
        <div class="col-md-4">
            <label for="status" class="form-label">Status</label>
            <form:select path="status" id="status" cssClass="form-control" required="true">
                <form:option value="NEW">New</form:option>
                <form:option value="ACTIVE">Active</form:option>
                <form:option value="RENEWED">Renewed</form:option>
                <form:option value="CLOSED">Closed</form:option>
            </form:select>
        </div>
        <div class="col-md-4">
            <div class="form-check mt-4">
                <form:checkbox path="autoRenewal" id="autoRenewal" cssClass="form-check-input"/>
                <label for="autoRenewal" class="form-check-label">Enable Auto-Renewal</label>
            </div>
        </div>
        <div class="col-md-4">
            <label for="renewalCount" class="form-label">Renewal Count</label>
            <form:input path="renewalCount" type="number" id="renewalCount" cssClass="form-control" readonly="true"/>
        </div>
        <div class="col-12">
            <label for="description" class="form-label">Description</label>
            <form:textarea path="description" id="description" cssClass="form-control" rows="3"/>
        </div>
        <div class="col-12">
            <button type="submit" class="btn btn-primary">Submit</button>
            <a href="/records" class="btn btn-secondary">Cancel</a>
        </div>
    </form:form>
</div>
</body>
</html>