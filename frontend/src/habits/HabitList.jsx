import React, { useCallback, useEffect, useState } from 'react';
import axios from 'axios';
import { jwtDecode } from 'jwt-decode';
import { List, ListItem, ListItemText, Typography, CircularProgress, Container, Button, Box } from '@mui/material';
import HabitDetail from './HabitDetail';
import UpdateHabitForm from './UpdateHabitForm';

const HabitList = () => {
  const [habits, setHabits] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [userId, setUserId] = useState(null);
  const [selectedHabitId, setSelectedHabitId] = useState(null);
  const [modalOpen, setModalOpen] = useState(false);
  const [updateModalOpen, setUpdateModalOpen] = useState(false);
  const [refresh, setRefresh] = useState(false);

  const fetchHabits = useCallback(async () => {
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
  }, [userId]);

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
    if (userId) {
      fetchHabits();
    }
  }, [userId, fetchHabits]);

  useEffect(() => {
    if (refresh) {
      fetchHabits();
      setRefresh(false);
    }
  }, [refresh, fetchHabits]);

  const handleOpenModal = (habitId) => {
    setSelectedHabitId(habitId);
    setModalOpen(true);
  };

  const handleCloseModal = () => {
    setModalOpen(false);
    setSelectedHabitId(null);
  };

  const handleOpenUpdateModal = (habitId) => {
    setSelectedHabitId(habitId);
    setUpdateModalOpen(true);
  };

  const handleCloseUpdateModal = () => {
    setUpdateModalOpen(false);
    setSelectedHabitId(null);
    setRefresh(true);
  };

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
            <Box>
              <Button variant="contained" color="primary" onClick={() => handleOpenUpdateModal(habit.id)}>
                Update Habit
              </Button>
              <Button variant="contained" color="primary" onClick={() => handleOpenModal(habit.id)}>
                View Details
              </Button> 
            </Box>
          </ListItem>
        ))}
      </List>
      {selectedHabitId && (
        <HabitDetail
          habitId={selectedHabitId}
          open={modalOpen}
          onClose={handleCloseModal}
        />
      )}
      {selectedHabitId && (
        <UpdateHabitForm
          habitId={selectedHabitId}
          open={updateModalOpen}
          onClose={handleCloseUpdateModal}
        />
      )}
    </Container>
  );
};

export default HabitList;
