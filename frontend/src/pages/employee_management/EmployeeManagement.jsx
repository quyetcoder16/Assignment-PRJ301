import React, { useState } from "react";
import {
  Button,
  TextField,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  Select,
  MenuItem,
  IconButton,
} from "@mui/material";
import { Delete, Visibility, Lock } from "@mui/icons-material";

const mockEmployees = [
  { id: 1, name: "John Doe", manager: "Alice", department: "HR", accounts: [] },
  { id: 2, name: "Jane Smith", manager: "Bob", department: "IT", accounts: [] },
];

const EmployeeManagement = () => {
  const [employees, setEmployees] = useState(mockEmployees);
  const [open, setOpen] = useState(false);
  const [openAccounts, setOpenAccounts] = useState(false);
  const [selectedEmployee, setSelectedEmployee] = useState(null);
  const [newEmployee, setNewEmployee] = useState({
    name: "",
    manager: "",
    department: "",
  });
  const [newAccount, setNewAccount] = useState({
    username: "",
    role: "",
    locked: false,
  });

  const handleAddEmployee = () => {
    setEmployees([
      ...employees,
      { ...newEmployee, id: employees.length + 1, accounts: [] },
    ]);
    setOpen(false);
  };

  const handleDeleteEmployee = (id) => {
    setEmployees(employees.filter((emp) => emp.id !== id));
  };

  const handleViewAccounts = (employee) => {
    setSelectedEmployee(employee);
    setOpenAccounts(true);
  };

  const handleAddAccount = () => {
    const updatedEmployees = employees.map((emp) =>
      emp.id === selectedEmployee.id
        ? { ...emp, accounts: [...emp.accounts, newAccount] }
        : emp
    );
    setEmployees(updatedEmployees);
    setNewAccount({ username: "", role: "", locked: false });
  };

  const handleDeleteAccount = (index) => {
    const updatedEmployees = employees.map((emp) =>
      emp.id === selectedEmployee.id
        ? {
            ...emp,
            accounts: emp.accounts.filter((_, i) => i !== index),
          }
        : emp
    );
    setEmployees(updatedEmployees);
  };

  const handleLockAccount = (index) => {
    const updatedEmployees = employees.map((emp) =>
      emp.id === selectedEmployee.id
        ? {
            ...emp,
            accounts: emp.accounts.map((account, i) =>
              i === index ? { ...account, locked: !account.locked } : account
            ),
          }
        : emp
    );
    setEmployees(updatedEmployees);
  };

  return (
    <div>
      <h2>Employee Management</h2>
      <Button variant="contained" onClick={() => setOpen(true)}>
        Add Employee
      </Button>
      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Name</TableCell>
              <TableCell>Manager</TableCell>
              <TableCell>Department</TableCell>
              <TableCell>Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {employees.map((emp) => (
              <TableRow key={emp.id}>
                <TableCell>{emp.name}</TableCell>
                <TableCell>{emp.manager}</TableCell>
                <TableCell>{emp.department}</TableCell>
                <TableCell>
                  <IconButton onClick={() => handleViewAccounts(emp)}>
                    <Visibility />
                  </IconButton>
                  <IconButton onClick={() => handleDeleteEmployee(emp.id)}>
                    <Delete />
                  </IconButton>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>

      <Dialog open={open} onClose={() => setOpen(false)}>
        <DialogTitle>Add Employee</DialogTitle>
        <DialogContent>
          <TextField
            label="Name"
            fullWidth
            margin="dense"
            onChange={(e) =>
              setNewEmployee({ ...newEmployee, name: e.target.value })
            }
          />
          <Select
            fullWidth
            margin="dense"
            value={newEmployee.manager}
            onChange={(e) =>
              setNewEmployee({ ...newEmployee, manager: e.target.value })
            }
          >
            <MenuItem value="Alice">Alice</MenuItem>
            <MenuItem value="Bob">Bob</MenuItem>
          </Select>
          <Select
            fullWidth
            margin="dense"
            value={newEmployee.department}
            onChange={(e) =>
              setNewEmployee({ ...newEmployee, department: e.target.value })
            }
          >
            <MenuItem value="HR">HR</MenuItem>
            <MenuItem value="IT">IT</MenuItem>
          </Select>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setOpen(false)}>Cancel</Button>
          <Button onClick={handleAddEmployee}>Add</Button>
        </DialogActions>
      </Dialog>

      {selectedEmployee && (
        <Dialog open={openAccounts} onClose={() => setOpenAccounts(false)}>
          <DialogTitle>Accounts for {selectedEmployee.name}</DialogTitle>
          <DialogContent>
            <TableContainer component={Paper}>
              <Table>
                <TableHead>
                  <TableRow>
                    <TableCell>Username</TableCell>
                    <TableCell>Role</TableCell>
                    <TableCell>Actions</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {selectedEmployee.accounts.map((account, index) => (
                    <TableRow key={index}>
                      <TableCell>{account.username}</TableCell>
                      <TableCell>{account.role}</TableCell>
                      <TableCell>
                        <IconButton onClick={() => handleLockAccount(index)}>
                          <Lock color={account.locked ? "error" : "inherit"} />
                        </IconButton>
                        <IconButton onClick={() => handleDeleteAccount(index)}>
                          <Delete />
                        </IconButton>
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </TableContainer>
            <TextField
              label="Username"
              fullWidth
              margin="dense"
              onChange={(e) =>
                setNewAccount({ ...newAccount, username: e.target.value })
              }
            />
            <Select
              fullWidth
              margin="dense"
              value={newAccount.role}
              onChange={(e) =>
                setNewAccount({ ...newAccount, role: e.target.value })
              }
            >
              <MenuItem value="Admin">Admin</MenuItem>
              <MenuItem value="User">User</MenuItem>
            </Select>
            <Button onClick={handleAddAccount}>Add Account</Button>
          </DialogContent>
          <DialogActions>
            <Button onClick={() => setOpenAccounts(false)}>Close</Button>
          </DialogActions>
        </Dialog>
      )}
    </div>
  );
};

export default EmployeeManagement;
