<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
    <title>Generate Report</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-4">
    <div class="row mb-3">
        <div class="col">
            <h2>Generate Fixed Deposit Report</h2>
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
            <a class="nav-link" href="/add">Add New FD</a>
        </li>
        <li class="nav-item">
            <a class="nav-link active" href="/report">Generate Report</a>
        </li>
    </ul>
    
    <form action="/generate-report" method="post" class="row g-3">
        <div class="col-md-6">
            <label for="fromDate" class="form-label">From Date (Optional)</label>
            <input type="date" name="fromDate" id="fromDate" class="form-control"/>
        </div>
        <div class="col-md-6">
            <label for="toDate" class="form-label">To Date (Optional)</label>
            <input type="date" name="toDate" id="toDate" class="form-control"/>
        </div>
        <div class="col-md-6">
            <label for="status" class="form-label">Status (Optional)</label>
            <select name="status" id="status" class="form-control">
                <option value="">All Status</option>
                <option value="NEW">New</option>
                <option value="ACTIVE">Active</option>
                <option value="RENEWED">Renewed</option>
                <option value="CLOSED">Closed</option>
            </select>
        </div>
        <div class="col-md-6">
            <label for="personName" class="form-label">Person Name (Optional)</label>
            <input type="text" name="personName" id="personName" class="form-control" placeholder="Enter person name (optional)"/>
        </div>
        <div class="col-12">
            <button type="submit" class="btn btn-success">Generate PDF Report</button>
        </div>
    </form>
</div>
</body>
</html>