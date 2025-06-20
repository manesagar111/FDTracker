<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
    <title>Fixed Deposits List</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>
<div class="container mt-4">
    <h2>Fixed Deposits</h2>
    
    <div class="row mb-3">
        <div class="col">
            <a href="/add" class="btn btn-primary">Add New FD</a>
            <a href="/report" class="btn btn-success">Generate Report</a>
            <div class="float-end">
                <span class="badge bg-secondary">Logged in as: <sec:authentication property="name"/></span>
                <a href="/logout" class="btn btn-outline-danger btn-sm ms-2">Logout</a>
            </div>
        </div>
    </div>
    
    <div class="card mb-4">
        <div class="card-body">
            <h5>Filter Options</h5>
            <div class="row g-3">
                <div class="col-md-3">
                    <label for="filterPerson" class="form-label">Person Name</label>
                    <input type="text" id="filterPerson" class="form-control" placeholder="Filter by person">
                </div>
                <div class="col-md-3">
                    <label for="filterBank" class="form-label">Bank Name</label>
                    <input type="text" id="filterBank" class="form-control" placeholder="Filter by bank">
                </div>
                <div class="col-md-2">
                    <label for="filterStatus" class="form-label">Status</label>
                    <select id="filterStatus" class="form-control">
                        <option value="">All Status</option>
                        <option value="NEW">NEW</option>
                        <option value="ACTIVE">ACTIVE</option>
                        <option value="MATURED">MATURED</option>
                    </select>
                </div>
                <div class="col-md-2">
                    <label for="filterMail" class="form-label">Mail Sent</label>
                    <select id="filterMail" class="form-control">
                        <option value="">All</option>
                        <option value="Yes">Yes</option>
                        <option value="No">No</option>
                    </select>
                </div>
                <div class="col-md-2 d-flex align-items-end">
                    <button type="button" id="clearFilters" class="btn btn-secondary">Clear</button>
                </div>
            </div>
        </div>
    </div>
    
    <div class="table-responsive">
        <table class="table table-striped" id="fdTable">
            <thead>
                <tr>
                    <th>Person Name</th>
                    <th>Bank Name</th>
                    <th>Account Number</th>
                    <th>From Date</th>
                    <th>To Date</th>
                    <th>Invested Amount</th>
                    <th>Return Amount</th>
                    <th>Description</th>
                    <th>Status</th>
                    <th>Mail Sent</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody id="fdTableBody">
                <c:forEach var="fd" items="${fixedDeposits}">
                    <tr>
                        <td>${fd.personName}</td>
                        <td>${fd.bankName}</td>
                        <td>${fd.accountNumber}</td>
                        <td>${fd.fromDate}</td>
                        <td>${fd.toDate}</td>
                        <td>${fd.investedAmount}</td>
                        <td>${fd.returnAmount}</td>
                        <td>${fd.description}</td>
                        <td><span class="badge bg-info">${fd.status}</span></td>
                        <td>
                            <c:choose>
                                <c:when test="${fd.mailSent}">
                                    <span class="badge bg-success">Yes</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge bg-warning">No</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <sec:authorize access="hasRole('ADMIN')">
                                <a href="/edit/${fd.id}" class="btn btn-primary btn-sm">Edit</a>
                                <a href="/delete/${fd.id}" class="btn btn-danger btn-sm" 
                                   onclick="return confirm('Are you sure?')">Remove</a>
                            </sec:authorize>
                            <sec:authorize access="hasRole('USER')">
                                <span class="text-muted">View Only</span>
                            </sec:authorize>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
    
    <div class="row mt-4">
        <div class="col-md-6">
            <div class="card">
                <div class="card-header">
                    <h5>Bank-wise Investment Distribution</h5>
                </div>
                <div class="card-body">
                    <canvas id="bankPieChart" width="300" height="300"></canvas>
                </div>
            </div>
        </div>
        <div class="col-md-6">
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

// Filter functionality
function filterTable() {
    const personFilter = document.getElementById('filterPerson').value.toLowerCase();
    const bankFilter = document.getElementById('filterBank').value.toLowerCase();
    const statusFilter = document.getElementById('filterStatus').value;
    const mailFilter = document.getElementById('filterMail').value;
    
    const rows = document.querySelectorAll('#fdTableBody tr');
    
    rows.forEach(row => {
        const cells = row.getElementsByTagName('td');
        const personName = cells[0].textContent.toLowerCase();
        const bankName = cells[1].textContent.toLowerCase();
        const status = cells[8].textContent.trim();
        const mailSent = cells[9].textContent.trim();
        
        const personMatch = personName.includes(personFilter);
        const bankMatch = bankName.includes(bankFilter);
        const statusMatch = !statusFilter || status === statusFilter;
        const mailMatch = !mailFilter || mailSent === mailFilter;
        
        if (personMatch && bankMatch && statusMatch && mailMatch) {
            row.style.display = '';
        } else {
            row.style.display = 'none';
        }
    });
}

// Add event listeners
document.getElementById('filterPerson').addEventListener('input', filterTable);
document.getElementById('filterBank').addEventListener('input', filterTable);
document.getElementById('filterStatus').addEventListener('change', filterTable);
document.getElementById('filterMail').addEventListener('change', filterTable);

// Clear filters
document.getElementById('clearFilters').addEventListener('click', function() {
    document.getElementById('filterPerson').value = '';
    document.getElementById('filterBank').value = '';
    document.getElementById('filterStatus').value = '';
    document.getElementById('filterMail').value = '';
    filterTable();
});
</script>
</body>
</html>