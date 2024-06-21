import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { jwtDecode } from 'jwt-decode';
import { List, ListItem, ListItemText, Typography, CircularProgress, Container } from '@mui/material';

const HabitList = () => {
  const [habits, setHabits] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [userId, setUserId] = useState(null);

  useEffect(() => {
    const fetchUserId = () => {
      const jwt = localStorage.getItem('jwt');
      if (jwt) {
        const decoded = jwtDecode(jwt);
        setUserId(decoded.userId);
      } else {
        setLoading(false);
        setError("JWT token not found");
      }
    };

    fetchUserId();
  }, []);

  useEffect(() => {
    const fetchHabits = async () => {
      const jwt = localStorage.getItem('jwt');
      if (!jwt) {
        setLoading(false);
        setError("JWT token not found");
        return;
      }
      try {
        const response = await axios.get(`http://localhost:8080/api/v1/habit/user/${userId}`, {
          headers: { Authorization: `Bearer ${jwt}` },
        });
        setHabits(response.data);
        setLoading(false);
      } catch (error) {
        setError(error);
        setLoading(false);
      }
    };

    if (userId) {
      fetchHabits();
    }
  }, [userId]);

  if (loading) {
    return <CircularProgress />;
  }

  if (error) {
    return <Typography color="error">Error loading habits: {error.message}</Typography>;
  }

  return (
    <Container>
      <Typography variant="h4" gutterBottom>
        Habits
      </Typography>
      <List>
        {habits.map(habit => (
          <ListItem key={habit.id}>
            <ListItemText
              primary={habit.name}
              secondary={
                <>
                  {habit.description && <Typography component="span">{habit.description}</Typography>}
                  {habit.trigger && <Typography component="span">Trigger: {habit.trigger}</Typography>}
                </>
              }
            />
          </ListItem>
        ))}
      </List>
    </Container>
  );
};

export default HabitList;
