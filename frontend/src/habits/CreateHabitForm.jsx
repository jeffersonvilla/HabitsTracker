import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { jwtDecode } from 'jwt-decode';
import { TextField, Button, Container, Typography, Box, Alert, Snackbar, MenuItem, IconButton } from '@mui/material';
import CategoryCreationDialog from './CategoryCreationDialog';
import EditIcon from '@mui/icons-material/esm/Edit';
import DeleteIcon from '@mui/icons-material/esm/Delete';
import DeleteHabitCategoryConfirmation from './DeleteHabitCategoryConfirmation';

const CreateHabitForm = () => {
    const [habit, setHabit] = useState({
        name: '',
        description: '',
        trigger: '',
        category: '',
    });

    const [errors, setErrors] = useState({});
    const [snackbarOpen, setSnackbarOpen] = useState(false);
    const [snackbarMessage, setSnackbarMessage] = useState('');
    const [userId, setUserId] = useState(null);
    const [categories, setCategories] = useState([]);
    const [categoryDialogOpen, setCategoryDialogOpen] = useState(false);
    const [categoryToUpdate, setCategoryToUpdate] = useState(null);
    const [confirmationDialogOpen, setConfirmationDialogOpen] = useState(false);
    const [categoryToDelete, setCategoryToDelete] = useState(null);

    useEffect(() => {
        const jwt = localStorage.getItem('jwt');
        if (jwt) {
            const decoded = jwtDecode(jwt);
            setUserId(decoded.userId);
        }
    }, []);

    useEffect(() => {
        if (userId) {
            const fetchCategories = async () => {
                const jwt = localStorage.getItem('jwt');
                if (!jwt) {
                    setSnackbarMessage("JWT token not found");
                    setSnackbarOpen(true);
                    return;
                }

                try {
                    const response = await axios.get(`http://localhost:8080/api/v1/habit-category/user/${userId}`, {
                        headers: { Authorization: `Bearer ${jwt}` },
                    });
                    setCategories(response.data);
                } catch (error) {
                    setSnackbarMessage("Error fetching categories");
                    setSnackbarOpen(true);
                }
            };

            fetchCategories();
        }
    }, [userId]);


    const handleChange = (e) => {
        const { name, value } = e.target;
        setHabit((prevHabit) => ({
            ...prevHabit,
            [name]: name === 'category' ? Number(value) : value,
        }));
    };

    const handleCloseSnackbar = () => {
        setSnackbarOpen(false);
    };

    const handleOpenCategoryDialog = () => {
        setCategoryDialogOpen(true);
    };
    
    const handleOpenUpdateDialog = (category) => {
        setCategoryToUpdate(category);
        setCategoryDialogOpen(true);
    };

    const handleOpenDeleteDialog = (category) => {
        setCategoryToDelete(category);
        setConfirmationDialogOpen(true);
    };

    const handleCloseCategoryDialog = () => {
        setCategoryDialogOpen(false);
    };

    const handleCloseConfirmationDialog = () => {
        setConfirmationDialogOpen(false);
        setCategoryToDelete(null);
    };

    const handleCategoryCreated = (newCategory) => {
        setCategories([...categories, newCategory]);
        setHabit({ ...habit, category: newCategory.categoryId });
    };

    const handleCategoryUpdated = (updatedCategory) => {
        setCategories(
            categories.map((category) => (category.categoryId === updatedCategory.categoryId ? updatedCategory : category))
        );
        if (habit.category === updatedCategory.categoryId) {
            setHabit({ ...habit, category: updatedCategory.categoryId });
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setErrors({});
        const jwt = localStorage.getItem('jwt');
        try {
            const response = await axios.post(
                'http://localhost:8080/api/v1/habit/',
                { ...habit, user: userId },
                {
                    headers: { Authorization: `Bearer ${jwt}` },
                }
            );
            console.log('Habit created:', response.data);
            setSnackbarMessage('Habit created successfully!');
            setSnackbarOpen(true);
            // Optionally, reset the form or handle the response as needed
        } catch (error) {
            //console.log(error.response.data);
            if (error.response) {
                if (error.response.status === 400) {
                    setSnackbarMessage(error.response.data.message || { message: 'Invalid input data' });
                    setSnackbarOpen(true);
                } else if (error.response.status === 403) {
                    setSnackbarMessage('You are not authorized to create a habit.');
                    setSnackbarOpen(true);
                } else {
                    setSnackbarMessage('Unexpected error occurred');
                    setSnackbarOpen(true);
                }
            } else {
                console.error('Error creating habit:', error);
                setSnackbarMessage('An error occurred. Please try again.');
                setSnackbarOpen(true);
            }
        }
    };

    const handleConfirmDelete = async () => {
        const jwt = localStorage.getItem('jwt');
        if (!jwt) {
           // setError("JWT token not found");
            return;
        }

        try {
            await axios.delete(`http://localhost:8080/api/v1/habit-category/${categoryToDelete.categoryId}`, {
                headers: { Authorization: `Bearer ${jwt}` },
            });
            setCategories(categories.filter(category => category.categoryId !== categoryToDelete.categoryId));
            handleCloseConfirmationDialog();
        } catch (error) {
            setSnackbarMessage(error.response?.data?.message || "Error deleting category");
            setSnackbarOpen(true);
        }
    };

    return (
        <Container maxWidth="sm">
            <Box sx={{ mt: 4 }}>
                <Typography variant="h4" component="h1" gutterBottom>
                    Create a New Habit
                </Typography>
                <form onSubmit={handleSubmit}>
                    <TextField
                        fullWidth
                        label="Name"
                        name="name"
                        value={habit.name}
                        onChange={handleChange}
                        required
                        margin="normal"
                        error={!!errors.name}
                        helperText={errors.name}
                    />
                    <TextField
                        fullWidth
                        label="Description"
                        name="description"
                        value={habit.description}
                        onChange={handleChange}
                        margin="normal"
                        error={!!errors.description}
                        helperText={errors.description}
                    />
                    <TextField
                        fullWidth
                        label="Trigger"
                        name="trigger"
                        value={habit.trigger}
                        onChange={handleChange}
                        margin="normal"
                        error={!!errors.trigger}
                        helperText={errors.trigger}
                    />
                    <TextField
                        select
                        fullWidth
                        label="Category"
                        name="category"
                        value={habit.category}
                        onChange={handleChange}
                        required
                        margin="normal"
                        error={!!errors.category}
                        helperText={errors.category}
                    >
                        {categories.map((category) => (
                            <MenuItem key={category.categoryId} value={category.categoryId}>
                                <Box sx={{ display: 'flex', justifyContent: 'space-between', width: '100%' }}>
                                    {category.name}
                                    <Box>
                                        <IconButton onClick={() => handleOpenUpdateDialog(category)} size="small" sx={{ ml: 2 }}>
                                            <EditIcon fontSize="small" />
                                        </IconButton>
                                        <IconButton onClick={() => handleOpenDeleteDialog(category)} size="small" sx={{ ml: 2 }}>
                                            <DeleteIcon fontSize="small" />
                                        </IconButton>
                                    </Box>
                                </Box>
                            </MenuItem>
                        ))}
                    </TextField>
                    <Box sx={{ display: 'flex', justifyContent: 'space-between', mt: 2 }}>
                        <Button variant="contained" onClick={handleOpenCategoryDialog} sx={{ mb: 2 }}>
                            Create New Category
                        </Button>
                        <Button variant="contained" color="primary" type="submit" sx={{ mt: 2 }}>
                            Create Habit
                        </Button>
                    </Box>
                    {Object.keys(errors).length > 0 && (
                        <Alert severity="error" sx={{ mt: 2 }}>
                            {Object.values(errors).map((err, index) => (
                                <div key={index}>{err}</div>
                            ))}
                        </Alert>
                    )}
                    
                </form>
                <Snackbar
                    open={snackbarOpen}
                    autoHideDuration={6000}
                    onClose={() => setSnackbarOpen(false)}
                    message={snackbarMessage}
                    anchorOrigin={{ vertical: 'top', horizontal: 'center' }}
                >
                    <Alert onClose={handleCloseSnackbar} severity="error" sx={{ width: '100%' }}>
                        {snackbarMessage}
                    </Alert>
                </Snackbar>
                <CategoryCreationDialog
                    open={categoryDialogOpen}
                    onClose={handleCloseCategoryDialog}
                    onCategoryCreated={handleCategoryCreated}
                    onCategoryUpdated={handleCategoryUpdated}
                    categoryToUpdate={categoryToUpdate}
                />
                <DeleteHabitCategoryConfirmation
                    open={confirmationDialogOpen}
                    onClose={handleCloseConfirmationDialog}
                    onConfirm={handleConfirmDelete}
                    message={`Are you sure you want to delete the category "${categoryToDelete?.name}"?`}
                />
            </Box>
        </Container>
    );
};

export default CreateHabitForm;
