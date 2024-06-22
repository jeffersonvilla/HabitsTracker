import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Modal, Box, Typography, CircularProgress } from '@mui/material';

const style = {
  position: 'absolute',
  top: '50%',
  left: '50%',
  transform: 'translate(-50%, -50%)',
  width: 400,
  bgcolor: 'background.paper',
  border: '2px solid #000',
  boxShadow: 24,
  p: 4,
};

const HabitDetail = ({ habitId, open, onClose }) => {
  const [habit, setHabit] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchHabitDetails = async () => {
      if (!habitId) return;

      const jwt = localStorage.getItem('jwt');
      if (!jwt) {
        setLoading(false);
        setError("JWT token not found");
        return;
      }

      try {
        const response = await axios.get(`http://localhost:8080/api/v1/habit/${habitId}`, {
          headers: { Authorization: `Bearer ${jwt}` },
        });
        setHabit(response.data);
        setLoading(false);
      } catch (error) {
        setError(error);
        setLoading(false);
      }
    };

    fetchHabitDetails();
  }, [habitId]);

  if (!open) {
    return null;
  }

  return (
    <Modal open={open} onClose={onClose}>
      <Box sx={style}>
        {loading ? (
          <CircularProgress />
        ) : error ? (
          <Typography color="error">Error loading habit details: {error.message}</Typography>
        ) : (
          <div>
            <Typography variant="h6">{habit.name}</Typography>
            <Typography variant="body1">{habit.description}</Typography>
            <Typography variant="body2">Trigger: {habit.trigger}</Typography>
            <Typography variant="body2">Category: {habit.category}</Typography>
            <Typography variant="body2">User: {habit.user}</Typography>
          </div>
        )}
      </Box>
    </Modal>
  );
};

export default HabitDetail;
