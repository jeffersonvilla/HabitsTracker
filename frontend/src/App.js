import React, { useEffect, useState } from 'react';
import { BrowserRouter as Router, Routes, Route, Link, Navigate } from 'react-router-dom';
import { AppBar, Toolbar, Button, Typography, Container } from '@mui/material';
import RegisterForm from './auth/RegisterForm'
import LoginForm from './auth/LoginForm';
import CreateHabitForm from './habits/CreateHabitForm';

function App() {
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  useEffect(() => {
    const jwt = localStorage.getItem('jwt');
    if (jwt) {
      setIsAuthenticated(true);
    } else {
      setIsAuthenticated(false);
    }
  }, []);

  const handleLogin = () => {
    setIsAuthenticated(true);
  };

  const handleLogout = () => {
    localStorage.removeItem('jwt');
    setIsAuthenticated(false);
  };
  
  return (
    <Router>
      <div className="App">
        <AppBar position="static">
          <Toolbar>
            <Typography variant="h6" style={{ flexGrow: 1 }}>
              MyApp
            </Typography>
            {!isAuthenticated && (
              <>
                <Button color="inherit" component={Link} to="/register">
                  Register
                </Button>
                <Button color="inherit" component={Link} to="/login">
                  Login
                </Button>
              </>
            )}
            {isAuthenticated && (
              <>
                <Button color="inherit" component={Link} to="/new-habit">
                  New Habit
                </Button>
                <Button color="inherit" onClick={handleLogout}>
                  Logout
                </Button>
              </>
            )}
          </Toolbar>
        </AppBar>
        <Container>
          <Routes>
            <Route path="/register" element={<RegisterForm />} />
            <Route path="/login" element={<LoginForm onLogin={handleLogin}/>} />
            <Route path="/new-habit" element={isAuthenticated ? <CreateHabitForm /> : <Navigate to="/login" />} />
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
