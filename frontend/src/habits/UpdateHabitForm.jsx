import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { jwtDecode } from 'jwt-decode';
import { Modal, Box, TextField, Button, CircularProgress, Alert, MenuItem } from '@mui/material';
import CategoryCreationDialog from './CategoryCreationDialog';

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

    const [userId, setUserId] = useState(null);
    const [categories, setCategories] = useState([]);
    const [categoryDialogOpen, setCategoryDialogOpen] = useState(false);  

    useEffect(() => {
        const jwt = localStorage.getItem('jwt');
        if (jwt) {
            const decoded = jwtDecode(jwt);
            setUserId(decoded.userId);
        }
    }, []);

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
                if(userId){
                    const categoryResponse = await axios.get(`http://localhost:8080/api/v1/habit-category/user/${userId}`, {
                        headers: { Authorization: `Bearer ${jwt}` },
                      });
                    setCategories(categoryResponse.data);
                }
                setLoading(false);
            } catch (error) {
                setError(error);
                setLoading(false);
            }
        };

        fetchHabitDetails();
    }, [habitId, userId]);

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

    const handleOpenCategoryDialog = () => {
        setCategoryDialogOpen(true);
    };
    
    const handleCloseCategoryDialog = () => {
        setCategoryDialogOpen(false);
    };

    const handleCategoryCreated = (newCategory) => {
        setCategories([...categories, newCategory]);
        setFormValues({ ...formValues, category: newCategory.id });
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
                                select
                                label="Category"
                                name="category"
                                value={formValues.category}
                                onChange={handleInputChange}
                                fullWidth
                                margin="normal"
                            >
                                {categories.map((category) => (
                                  <MenuItem key={category.id} value={category.id}>
                                    {category.name}
                                  </MenuItem>
                                ))}
                            </TextField>
                            <Button variant="contained" onClick={handleOpenCategoryDialog} sx={{ mb: 2 }}>
                                Create New Category
                            </Button>
                            <Button type="submit" variant="contained" color="primary" fullWidth>
                                Update Habit
                            </Button>
                        </form>
                    </>
                )}
                <CategoryCreationDialog
                    open={categoryDialogOpen}
                    onClose={handleCloseCategoryDialog}
                    onCategoryCreated={handleCategoryCreated}
                />
            </Box>
        </Modal>
    );
};

export default UpdateHabitForm;
