<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
    <title>FD Records</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-4">
    <div class="row mb-3">
        <div class="col">
            <h2>Fixed Deposit Records</h2>
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
            <a class="nav-link active" href="/records">Records</a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="/add">Add New FD</a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="/report">Generate Report</a>
        </li>
    </ul>
    
    <!-- Filter Options -->
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
    
    <!-- Records Table -->
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
                    <th>Auto-Renewal</th>
                    <th>Renewals</th>
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
                                <c:when test="${fd.autoRenewal}">
                                    <span class="badge bg-success">Enabled</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge bg-secondary">Disabled</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <span class="badge bg-primary">${fd.renewalCount}</span>
                            <c:if test="${fd.lastRenewalDate != null}">
                                <br><small class="text-muted">Last: ${fd.lastRenewalDate}</small>
                            </c:if>
                        </td>
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
                                <c:if test="${fd.autoRenewal && fd.status == 'ACTIVE'}">
                                    <a href="/renew/${fd.id}" class="btn btn-success btn-sm">Renew</a>
                                </c:if>
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
</div>

<script>
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