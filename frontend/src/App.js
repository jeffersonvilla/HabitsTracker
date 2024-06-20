import React from 'react';
import { BrowserRouter as Router, Routes, Route, Link, Navigate } from 'react-router-dom';
import { AppBar, Toolbar, Button, Typography, Container } from '@mui/material';
import RegisterForm from './auth/RegisterForm'
import LoginForm from './auth/LoginForm';

function App() {
  return (
    <Router>
      <div className="App">
        <AppBar position="static">
          <Toolbar>
            <Typography variant="h6" style={{ flexGrow: 1 }}>
              MyApp
            </Typography>
            <Button color="inherit" component={Link} to="/register">
              Register
            </Button>
            <Button color="inherit" component={Link} to="/login">
              Login
            </Button>
          </Toolbar>
        </AppBar>
        <Container>
          <Routes>
            <Route path="/register" element={<RegisterForm />} />
            <Route path="/login" element={<LoginForm />} />
            <Route path="/" element={<Navigate to="/login" />} />
            <Route path="*" element={<Typography variant="h4" component="div" style={{ marginTop: '20px' }}>
              Page Not Found
            </Typography>} />
          </Routes>
        </Container>
      </div>
    </Router>
  );
}

export default App;
