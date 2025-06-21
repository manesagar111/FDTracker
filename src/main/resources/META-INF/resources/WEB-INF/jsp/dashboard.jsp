<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
    <title>FD Tracker - Dashboard</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>
<div class="container mt-4">
    <div class="row mb-3">
        <div class="col">
            <h2>FD Tracker Dashboard</h2>
            <div class="float-end">
                <span class="badge bg-secondary">Logged in as: <sec:authentication property="name"/></span>
                <a href="/logout" class="btn btn-outline-danger btn-sm ms-2">Logout</a>
            </div>
        </div>
    </div>
    
    <!-- Navigation Tabs -->
    <ul class="nav nav-tabs mb-4">
        <li class="nav-item">
            <a class="nav-link active" href="/">Dashboard</a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="/records">Records</a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="/add">Add New FD</a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="/report">Generate Report</a>
        </li>
    </ul>
    
    <!-- Dashboard Content -->
    <div class="row">
        <div class="col-md-8">
            <div class="card">
                <div class="card-header">
                    <h5>Bank-wise Investment Distribution</h5>
                </div>
                <div class="card-body">
                    <canvas id="bankPieChart" width="500" height="400"></canvas>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card">
                <div class="card-header">
                    <h5>Investment Summary</h5>
                </div>
                <div class="card-body">
                    <table class="table table-sm">
                        <thead>
                            <tr>
                                <th>Bank</th>
                                <th>Amount</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:set var="totalAmount" value="0" />
                            <c:forEach var="entry" items="${bankWiseData}">
                                <tr>
                                    <td>${entry.key}</td>
                                    <td>₹${entry.value}</td>
                                </tr>
                                <c:set var="totalAmount" value="${totalAmount + entry.value}" />
                            </c:forEach>
                        </tbody>
                        <tfoot>
                            <tr class="table-dark">
                                <th>Total</th>
                                <th>₹${totalAmount}</th>
                            </tr>
                        </tfoot>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
const ctx = document.getElementById('bankPieChart').getContext('2d');
const bankData = {
    <c:forEach var="entry" items="${bankWiseData}" varStatus="status">
        '${entry.key}': ${entry.value}<c:if test="${!status.last}">,</c:if>
    </c:forEach>
};

const labels = Object.keys(bankData);
const data = Object.values(bankData);
const colors = ['#FF6384', '#36A2EB', '#FFCE56', '#4BC0C0', '#9966FF', '#FF9F40'];

if (labels.length > 0) {
    new Chart(ctx, {
        type: 'pie',
        data: {
            labels: labels,
            datasets: [{
                data: data,
                backgroundColor: colors.slice(0, labels.length),
                borderWidth: 2,
                borderColor: '#fff'
            }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: {
                    position: 'bottom'
                },
                tooltip: {
                    callbacks: {
                        label: function(context) {
                            return context.label + ': ₹' + context.parsed.toLocaleString();
                        }
                    }
                }
            }
        }
    });
}
</script>
</body>
</html>