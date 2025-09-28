// Finance Management System - JavaScript Functions

// Global variables
let currentSection = 'users';
let currentSubSection = 'list';

// Initialize the application
document.addEventListener('DOMContentLoaded', function() {
    initializeApp();
});

function initializeApp() {
    // Add event listeners
    setupEventListeners();
    
    // Show initial section
    showSection('users');
    
    // Load initial data
    loadUsers();
}

function setupEventListeners() {
    // Form submissions
    document.getElementById('addUserForm').addEventListener('submit', handleAddUser);
    document.getElementById('searchUserForm').addEventListener('submit', handleSearchUser);
    document.getElementById('editUserForm').addEventListener('submit', handleEditUser);
    document.getElementById('addTransactionForm').addEventListener('submit', handleAddTransaction);
    document.getElementById('userTransactionForm').addEventListener('submit', handleUserTransactions);
    document.getElementById('editTransactionForm').addEventListener('submit', handleEditTransaction);
    document.getElementById('userReportForm').addEventListener('submit', handleUserReport);
    document.getElementById('monthlyReportForm').addEventListener('submit', handleMonthlyReport);
    document.getElementById('weeklyReportForm').addEventListener('submit', handleWeeklyReport);
}

// Navigation Functions
function showSection(section) {
    // Hide all sections
    document.querySelectorAll('.content-section').forEach(el => {
        el.classList.remove('active');
    });
    
    // Remove active class from nav buttons
    document.querySelectorAll('.nav-btn').forEach(btn => {
        btn.classList.remove('active');
    });
    
    // Show selected section
    const sectionElement = document.getElementById(section + 'Section');
    if (sectionElement) {
        sectionElement.classList.add('active');
    }
    
    // Add active class to nav button
    const navButton = document.querySelector(`[onclick="showSection('${section}')"]`);
    if (navButton) {
        navButton.classList.add('active');
    }
    
    currentSection = section;
    
    // Show appropriate sub-section
    if (section === 'users') {
        showUserSection('list');
    } else if (section === 'transactions') {
        showTransactionSection('list');
    }
}

function showUserSection(section) {
    hideAllUserSections();
    const sectionElement = document.getElementById(section + 'UserSection');
    if (sectionElement) {
        sectionElement.style.display = 'block';
    }
    
    // Update sub-nav buttons
    updateSubNavButtons('user', section);
    
    if (section === 'list') {
        loadUsers();
    }
}

function showTransactionSection(section) {
    hideAllTransactionSections();
    const sectionElement = document.getElementById(section + 'TransactionSection');
    if (sectionElement) {
        sectionElement.style.display = 'block';
    }
    
    // Update sub-nav buttons
    updateSubNavButtons('transaction', section);
    
    if (section === 'list') {
        loadTransactions();
    }
}

function hideAllUserSections() {
    document.getElementById('addUserSection').style.display = 'none';
    document.getElementById('listUserSection').style.display = 'none';
    document.getElementById('searchUserSection').style.display = 'none';
    document.getElementById('editUserSection').style.display = 'none';
}

function hideAllTransactionSections() {
    document.getElementById('addTransactionSection').style.display = 'none';
    document.getElementById('listTransactionSection').style.display = 'none';
    document.getElementById('userTransactionSection').style.display = 'none';
    document.getElementById('editTransactionSection').style.display = 'none';
}

function updateSubNavButtons(type, activeSection) {
    const buttons = document.querySelectorAll(`.sub-nav-btn[onclick*="${type}"]`);
    buttons.forEach(btn => {
        btn.classList.remove('active');
        if (btn.onclick.toString().includes(activeSection)) {
            btn.classList.add('active');
        }
    });
}

// API Functions
async function apiCall(url, options = {}) {
    try {
        const response = await fetch(url, {
            headers: {
                'Content-Type': 'application/json',
                ...options.headers
            },
            ...options
        });
        
        const result = await response.json();
        return result;
    } catch (error) {
        console.error('API Error:', error);
        throw error;
    }
}

// User Management Functions
async function handleAddUser(e) {
    e.preventDefault();
    
    const formData = {
        name: document.getElementById('addName').value,
        email: document.getElementById('addEmail').value,
        phoneNumber: document.getElementById('addPhone').value,
        address: document.getElementById('addAddress').value,
        bankAccountNumber: document.getElementById('addBank').value
    };
    
    try {
        showLoading(true);
        const result = await apiCall('/api/user/add', {
            method: 'POST',
            body: JSON.stringify(formData)
        });
        
        if (result.success) {
            showMessage('User added successfully!', 'success');
            document.getElementById('addUserForm').reset();
            loadUsers();
        } else {
            showMessage(result.message || 'Error adding user', 'error');
        }
    } catch (error) {
        showMessage('Network error: ' + error.message, 'error');
    } finally {
        showLoading(false);
    }
}

async function handleSearchUser(e) {
    e.preventDefault();
    
    const userId = document.getElementById('searchUserId').value;
    
    try {
        showLoading(true);
        const result = await apiCall(`/api/user/${userId}`);
        
        if (result.success) {
            displayUser(result.data);
        } else {
            showMessage(result.message || 'User not found', 'error');
            document.getElementById('searchUserResult').style.display = 'none';
        }
    } catch (error) {
        showMessage('Network error: ' + error.message, 'error');
        document.getElementById('searchUserResult').style.display = 'none';
    } finally {
        showLoading(false);
    }
}

async function handleEditUser(e) {
    e.preventDefault();
    
    const userId = document.getElementById('editUserId').value;
    const formData = {
        name: document.getElementById('editName').value,
        email: document.getElementById('editEmail').value,
        phoneNumber: document.getElementById('editPhone').value,
        address: document.getElementById('editAddress').value,
        bankAccountNumber: document.getElementById('editBank').value
    };
    
    try {
        showLoading(true);
        const result = await apiCall(`/api/user/edit/${userId}`, {
            method: 'PUT',
            body: JSON.stringify(formData)
        });
        
        if (result.success) {
            showMessage('User updated successfully!', 'success');
            document.getElementById('editUserForm').reset();
            loadUsers();
        } else {
            showMessage(result.message || 'Error updating user', 'error');
        }
    } catch (error) {
        showMessage('Network error: ' + error.message, 'error');
    } finally {
        showLoading(false);
    }
}

async function editUser(userId) {
    try {
        showLoading(true);
        const result = await apiCall(`/api/user/${userId}`);
        
        if (result.success) {
            const user = result.data;
            document.getElementById('editUserId').value = user.id;
            document.getElementById('editName').value = user.name;
            document.getElementById('editEmail').value = user.email || '';
            document.getElementById('editPhone').value = user.phoneNumber || '';
            document.getElementById('editAddress').value = user.address || '';
            document.getElementById('editBank').value = user.bankAccountNumber || '';
            
            showUserSection('edit');
            showMessage('User data loaded for editing!', 'success');
        } else {
            showMessage(result.message || 'Error loading user data', 'error');
        }
    } catch (error) {
        showMessage('Network error: ' + error.message, 'error');
    } finally {
        showLoading(false);
    }
}

async function deleteUser(userId) {
    if (confirm('Are you sure you want to delete this user?')) {
        try {
            showLoading(true);
            const result = await apiCall(`/api/user/delete/${userId}`, {
                method: 'DELETE'
            });
            
            if (result.success) {
                showMessage('User deleted successfully!', 'success');
                loadUsers();
            } else {
                showMessage(result.message || 'Error deleting user', 'error');
            }
        } catch (error) {
            showMessage('Network error: ' + error.message, 'error');
        } finally {
            showLoading(false);
        }
    }
}

// Transaction Management Functions
async function handleAddTransaction(e) {
    e.preventDefault();
    
    const formData = {
        userId: parseInt(document.getElementById('addTransactionUserId').value),
        amount: parseFloat(document.getElementById('addTransactionAmount').value),
        type: document.getElementById('addTransactionType').value,
        description: document.getElementById('addTransactionDescription').value
    };
    
    try {
        showLoading(true);
        const result = await apiCall('/api/transaction/add', {
            method: 'POST',
            body: JSON.stringify(formData)
        });
        
        if (result.success) {
            showMessage('Transaction added successfully!', 'success');
            document.getElementById('addTransactionForm').reset();
            loadTransactions();
        } else {
            showMessage(result.message || 'Error adding transaction', 'error');
        }
    } catch (error) {
        showMessage('Network error: ' + error.message, 'error');
    } finally {
        showLoading(false);
    }
}

async function handleUserTransactions(e) {
    e.preventDefault();
    
    const userId = document.getElementById('userTransactionUserId').value;
    
    try {
        showLoading(true);
        const result = await apiCall(`/api/transaction/user/${userId}`);
        
        if (result.success) {
            displayUserTransactions(userId, result.data);
        } else {
            showMessage(result.message || 'Error loading transactions', 'error');
            document.getElementById('userTransactionResult').style.display = 'none';
        }
    } catch (error) {
        showMessage('Network error: ' + error.message, 'error');
        document.getElementById('userTransactionResult').style.display = 'none';
    } finally {
        showLoading(false);
    }
}

async function handleEditTransaction(e) {
    e.preventDefault();
    
    const transactionId = document.getElementById('editTransactionId').value;
    const formData = {
        amount: parseFloat(document.getElementById('editTransactionAmount').value),
        type: document.getElementById('editTransactionType').value,
        description: document.getElementById('editTransactionDescription').value
    };
    
    try {
        showLoading(true);
        const result = await apiCall(`/api/transaction/edit/${transactionId}`, {
            method: 'PUT',
            body: JSON.stringify(formData)
        });
        
        if (result.success) {
            showMessage('Transaction updated successfully!', 'success');
            document.getElementById('editTransactionForm').reset();
            loadTransactions();
        } else {
            showMessage(result.message || 'Error updating transaction', 'error');
        }
    } catch (error) {
        showMessage('Network error: ' + error.message, 'error');
    } finally {
        showLoading(false);
    }
}

async function editTransaction(transactionId) {
    try {
        showLoading(true);
        const result = await apiCall(`/api/transaction/all`);
        
        if (result.success) {
            const transaction = result.data.find(t => t.id === transactionId);
            if (transaction) {
                document.getElementById('editTransactionId').value = transaction.id;
                document.getElementById('editTransactionAmount').value = transaction.amount;
                document.getElementById('editTransactionType').value = transaction.type;
                document.getElementById('editTransactionDescription').value = transaction.description;
                
                showTransactionSection('edit');
                showMessage('Transaction data loaded for editing!', 'success');
            } else {
                showMessage('Transaction not found', 'error');
            }
        } else {
            showMessage(result.message || 'Error loading transaction data', 'error');
        }
    } catch (error) {
        showMessage('Network error: ' + error.message, 'error');
    } finally {
        showLoading(false);
    }
}

async function deleteTransaction(transactionId) {
    if (confirm('Are you sure you want to delete this transaction?')) {
        try {
            showLoading(true);
            const result = await apiCall(`/api/transaction/delete/${transactionId}`, {
                method: 'DELETE'
            });
            
            if (result.success) {
                showMessage('Transaction deleted successfully!', 'success');
                loadTransactions();
            } else {
                showMessage(result.message || 'Error deleting transaction', 'error');
            }
        } catch (error) {
            showMessage('Network error: ' + error.message, 'error');
        } finally {
            showLoading(false);
        }
    }
}

// Report Functions
async function handleUserReport(e) {
    e.preventDefault();
    
    const userId = document.getElementById('reportUserId').value;
    
    try {
        showLoading(true);
        const result = await apiCall(`/api/reports/balance?userId=${userId}`);
        
        if (result.success) {
            displayUserReport(result.data);
        } else {
            showMessage(result.message || 'Error generating report', 'error');
        }
    } catch (error) {
        showMessage('Network error: ' + error.message, 'error');
    } finally {
        showLoading(false);
    }
}

async function handleMonthlyReport(e) {
    e.preventDefault();
    
    const userId = document.getElementById('monthlyReportUserId').value;
    const year = document.getElementById('monthlyReportYear').value;
    const month = document.getElementById('monthlyReportMonth').value;
    
    try {
        showLoading(true);
        const result = await apiCall(`/api/reports/monthly?userId=${userId}&year=${year}&month=${month}`);
        
        if (result.success) {
            displayMonthlyReport(result.data);
        } else {
            showMessage(result.message || 'Error generating report', 'error');
        }
    } catch (error) {
        showMessage('Network error: ' + error.message, 'error');
    } finally {
        showLoading(false);
    }
}

async function handleWeeklyReport(e) {
    e.preventDefault();
    
    const userId = document.getElementById('weeklyReportUserId').value;
    const year = document.getElementById('weeklyReportYear').value;
    const month = document.getElementById('weeklyReportMonth').value;
    const week = document.getElementById('weeklyReportWeek').value;
    
    try {
        showLoading(true);
        const result = await apiCall(`/api/reports/weekly?userId=${userId}&year=${year}&month=${month}&week=${week}`);
        
        if (result.success) {
            displayWeeklyReport(result.data);
        } else {
            showMessage(result.message || 'Error generating report', 'error');
        }
    } catch (error) {
        showMessage('Network error: ' + error.message, 'error');
    } finally {
        showLoading(false);
    }
}

// Data Loading Functions
async function loadUsers() {
    try {
        showLoading(true);
        const result = await apiCall('/api/user/all');
        
        if (result.success) {
            displayUsers(result.data);
        } else {
            showMessage(result.message || 'Error loading users', 'error');
        }
    } catch (error) {
        showMessage('Network error: ' + error.message, 'error');
    } finally {
        showLoading(false);
    }
}

async function loadTransactions() {
    try {
        showLoading(true);
        const result = await apiCall('/api/transaction/all');
        
        if (result.success) {
            displayTransactions(result.data);
        } else {
            showMessage(result.message || 'Error loading transactions', 'error');
        }
    } catch (error) {
        showMessage('Network error: ' + error.message, 'error');
    } finally {
        showLoading(false);
    }
}

// Display Functions
function displayUsers(users) {
    const container = document.getElementById('usersTable');
    
    if (users.length === 0) {
        container.innerHTML = '<div class="no-data">No users found</div>';
        return;
    }
    
    const tableHTML = `
        <div class="table-container">
            <table class="table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Email</th>
                        <th>Phone</th>
                        <th>Address</th>
                        <th>Bank Account</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    ${users.map(user => `
                        <tr>
                            <td>${user.id}</td>
                            <td>${user.name}</td>
                            <td>${user.email || '-'}</td>
                            <td>${user.phoneNumber || '-'}</td>
                            <td>${user.address || '-'}</td>
                            <td>${user.bankAccountNumber || '-'}</td>
                            <td>
                                <button class="btn btn-primary" onclick="editUser(${user.id})">Edit</button>
                                <button class="btn btn-danger" onclick="deleteUser(${user.id})">Delete</button>
                            </td>
                        </tr>
                    `).join('')}
                </tbody>
            </table>
        </div>
    `;
    
    container.innerHTML = tableHTML;
}

function displayTransactions(transactions) {
    const container = document.getElementById('transactionsTable');
    
    if (transactions.length === 0) {
        container.innerHTML = '<div class="no-data">No transactions found</div>';
        return;
    }
    
    const tableHTML = `
        <div class="table-container">
            <table class="table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Amount</th>
                        <th>Type</th>
                        <th>Description</th>
                        <th>Date</th>
                        <th>User</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    ${transactions.map(transaction => {
                        const date = new Date(transaction.transactionDate).toLocaleString();
                        return `
                            <tr>
                                <td>${transaction.id}</td>
                                <td>$${transaction.amount.toFixed(2)}</td>
                                <td><span class="badge ${transaction.type === 'INCOME' ? 'badge-success' : 'badge-danger'}">${transaction.type}</span></td>
                                <td>${transaction.description}</td>
                                <td>${date}</td>
                                <td>${transaction.userName || 'Unknown'}</td>
                                <td>
                                    <button class="btn btn-primary" onclick="editTransaction(${transaction.id})">Edit</button>
                                    <button class="btn btn-danger" onclick="deleteTransaction(${transaction.id})">Delete</button>
                                </td>
                            </tr>
                        `;
                    }).join('')}
                </tbody>
            </table>
        </div>
    `;
    
    container.innerHTML = tableHTML;
}

function displayUser(user) {
    const container = document.getElementById('userInfo');
    container.innerHTML = `
        <div class="user-info">
            <p><strong>ID:</strong> ${user.id}</p>
            <p><strong>Name:</strong> ${user.name}</p>
            <p><strong>Email:</strong> ${user.email || 'Not provided'}</p>
            <p><strong>Phone:</strong> ${user.phoneNumber || 'Not provided'}</p>
            <p><strong>Address:</strong> ${user.address || 'Not provided'}</p>
            <p><strong>Bank Account:</strong> ${user.bankAccountNumber || 'Not provided'}</p>
        </div>
    `;
    
    document.getElementById('searchUserResult').style.display = 'block';
    showMessage('User found successfully!', 'success');
}

function displayUserTransactions(userId, transactions) {
    document.getElementById('displayTransactionUserId').textContent = userId;
    document.getElementById('userTransactionResult').style.display = 'block';
    
    const container = document.getElementById('userTransactionTable');
    
    if (transactions.length === 0) {
        container.innerHTML = '<div class="no-data">No transactions found for this user.</div>';
        return;
    }
    
    const tableHTML = `
        <div class="table-container">
            <table class="table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Amount</th>
                        <th>Type</th>
                        <th>Description</th>
                        <th>Date</th>
                    </tr>
                </thead>
                <tbody>
                    ${transactions.map(transaction => {
                        const date = new Date(transaction.transactionDate).toLocaleString();
                        return `
                            <tr>
                                <td>${transaction.id}</td>
                                <td>$${transaction.amount.toFixed(2)}</td>
                                <td><span class="badge ${transaction.type === 'INCOME' ? 'badge-success' : 'badge-danger'}">${transaction.type}</span></td>
                                <td>${transaction.description}</td>
                                <td>${date}</td>
                            </tr>
                        `;
                    }).join('')}
                </tbody>
            </table>
        </div>
    `;
    
    container.innerHTML = tableHTML;
    showMessage('Transactions loaded successfully!', 'success');
}

function displayUserReport(data) {
    const container = document.getElementById('userReportContent');
    container.innerHTML = `
        <div class="report-summary">
            <h4>User: ${data.userName}</h4>
            <div class="financial-summary">
                <div class="summary-item">
                    <span class="label">Total Income:</span>
                    <span class="value income">$${data.income.toFixed(2)}</span>
                </div>
                <div class="summary-item">
                    <span class="label">Total Expenses:</span>
                    <span class="value expense">$${data.expenses.toFixed(2)}</span>
                </div>
                <div class="summary-item">
                    <span class="label">Net Balance:</span>
                    <span class="value ${data.balance >= 0 ? 'income' : 'expense'}">$${data.balance.toFixed(2)}</span>
                </div>
            </div>
        </div>
    `;
    
    document.getElementById('userReportResult').classList.add('active');
    showMessage('User report generated successfully!', 'success');
}

function displayMonthlyReport(data) {
    const container = document.getElementById('monthlyReportContent');
    
    let content = `
        <div class="report-header">
            <h4>Monthly Report for User ID: ${data.userId}</h4>
            <p>Year: ${data.year}, Month: ${data.month}</p>
        </div>
        <div class="financial-summary">
            <div class="summary-item">
                <span class="label">Total Income:</span>
                <span class="value income">$${data.monthlyIncome.toFixed(2)}</span>
            </div>
            <div class="summary-item">
                <span class="label">Total Expenses:</span>
                <span class="value expense">$${data.monthlyExpenses.toFixed(2)}</span>
            </div>
            <div class="summary-item">
                <span class="label">Monthly Balance:</span>
                <span class="value ${data.monthlyBalance >= 0 ? 'income' : 'expense'}">$${data.monthlyBalance.toFixed(2)}</span>
            </div>
        </div>
        <h4>Transactions (${data.transactions.length})</h4>
    `;
    
    if (data.transactions.length > 0) {
        content += `
            <div class="table-container">
                <table class="table">
                    <thead>
                        <tr>
                            <th>Description</th>
                            <th>Amount</th>
                            <th>Type</th>
                            <th>Date</th>
                        </tr>
                    </thead>
                    <tbody>
                        ${data.transactions.map(transaction => `
                            <tr>
                                <td>${transaction.description}</td>
                                <td>$${transaction.amount.toFixed(2)}</td>
                                <td><span class="badge ${transaction.type === 'INCOME' ? 'badge-success' : 'badge-danger'}">${transaction.type}</span></td>
                                <td>${new Date(transaction.transactionDate).toLocaleDateString()}</td>
                            </tr>
                        `).join('')}
                    </tbody>
                </table>
            </div>
        `;
    } else {
        content += '<div class="no-data">No transactions found for this month.</div>';
    }
    
    container.innerHTML = content;
    document.getElementById('monthlyReportResult').classList.add('active');
    showMessage('Monthly report generated successfully!', 'success');
}

function displayWeeklyReport(data) {
    const container = document.getElementById('weeklyReportContent');
    
    let content = `
        <div class="report-header">
            <h4>Weekly Report for User ID: ${data.userId}</h4>
            <p>Year: ${data.year}, Month: ${data.month}, Week: ${data.week}</p>
            <p><strong>Week Period:</strong> ${data.weekStart} to ${data.weekEnd}</p>
        </div>
        <div class="financial-summary">
            <div class="summary-item">
                <span class="label">Total Income:</span>
                <span class="value income">$${data.weeklyIncome.toFixed(2)}</span>
            </div>
            <div class="summary-item">
                <span class="label">Total Expenses:</span>
                <span class="value expense">$${data.weeklyExpenses.toFixed(2)}</span>
            </div>
            <div class="summary-item">
                <span class="label">Weekly Balance:</span>
                <span class="value ${data.weeklyBalance >= 0 ? 'income' : 'expense'}">$${data.weeklyBalance.toFixed(2)}</span>
            </div>
        </div>
        <h4>Transactions (${data.transactions.length})</h4>
    `;
    
    if (data.transactions.length > 0) {
        content += `
            <div class="table-container">
                <table class="table">
                    <thead>
                        <tr>
                            <th>Description</th>
                            <th>Amount</th>
                            <th>Type</th>
                            <th>Date</th>
                        </tr>
                    </thead>
                    <tbody>
                        ${data.transactions.map(transaction => `
                            <tr>
                                <td>${transaction.description}</td>
                                <td>$${transaction.amount.toFixed(2)}</td>
                                <td><span class="badge ${transaction.type === 'INCOME' ? 'badge-success' : 'badge-danger'}">${transaction.type}</span></td>
                                <td>${new Date(transaction.transactionDate).toLocaleDateString()}</td>
                            </tr>
                        `).join('')}
                    </tbody>
                </table>
            </div>
        `;
    } else {
        content += '<div class="no-data">No transactions found for this week.</div>';
    }
    
    container.innerHTML = content;
    document.getElementById('weeklyReportResult').classList.add('active');
    showMessage('Weekly report generated successfully!', 'success');
}

// Utility Functions
function showMessage(message, type) {
    const messageDiv = document.createElement('div');
    messageDiv.className = `message ${type}`;
    messageDiv.textContent = message;
    
    document.body.appendChild(messageDiv);
    
    setTimeout(() => {
        messageDiv.remove();
    }, 5000);
}

function showLoading(show) {
    const existingLoader = document.querySelector('.loading-overlay');
    if (show && !existingLoader) {
        const loader = document.createElement('div');
        loader.className = 'loading-overlay';
        loader.innerHTML = '<div class="loading"></div>';
        loader.style.cssText = `
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0,0,0,0.5);
            display: flex;
            justify-content: center;
            align-items: center;
            z-index: 9999;
        `;
        document.body.appendChild(loader);
    } else if (!show && existingLoader) {
        existingLoader.remove();
    }
}
