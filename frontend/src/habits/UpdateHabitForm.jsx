import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Modal, Box, TextField, Button, CircularProgress, Alert } from '@mui/material';

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

const UpdateHabitForm = ({ habitId, open, onClose }) => {
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [formValues, setFormValues] = useState({
        name: '',
        description: '',
        trigger: '',
        category: '',
    });

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
                setFormValues({
                    name: response.data.name,
                    description: response.data.description,
                    trigger: response.data.trigger,
                    category: response.data.category,
                });
                setLoading(false);
            } catch (error) {
                setError(error);
                setLoading(false);
            }
        };

        fetchHabitDetails();
    }, [habitId]);

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormValues({
            ...formValues,
            [name]: value,
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        const jwt = localStorage.getItem('jwt');
        if (!jwt) {
            setError("JWT token not found");
            return;
        }

        try {
            await axios.put(`http://localhost:8080/api/v1/habit/${habitId}`, formValues, {
                headers: { Authorization: `Bearer ${jwt}` },
            });
            onClose();
        } catch (error) {
            setError(error.response?.data?.message || "Error updating habit");
        }
    };

    if (!open) {
        return null;
    }

    return (
        <Modal open={open} onClose={onClose}>
            <Box sx={style}>
                {loading ? (
                    <CircularProgress />
                ) : (
                    <>
                        {error && <Alert severity="error">{error}</Alert>}
                        <form onSubmit={handleSubmit}>
                            <TextField
                                label="Name"
                                name="name"
                                value={formValues.name}
                                onChange={handleInputChange}
                                fullWidth
                                margin="normal"
                            />
                            <TextField
                                label="Description"
                                name="description"
                                value={formValues.description}
                                onChange={handleInputChange}
                                fullWidth
                                margin="normal"
                            />
                            <TextField
                                label="Trigger"
                                name="trigger"
                                value={formValues.trigger}
                                onChange={handleInputChange}
                                fullWidth
                                margin="normal"
                            />
                            <TextField
                                label="Category"
                                name="category"
                                value={formValues.category}
                                onChange={handleInputChange}
                                fullWidth
                                margin="normal"
                            />
                            <Button type="submit" variant="contained" color="primary" fullWidth>
                                Update Habit
                            </Button>
                        </form>
                    </>
                )}
            </Box>
        </Modal>
    );
};

export default UpdateHabitForm;
