import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { jwtDecode } from 'jwt-decode';
import { Modal, Box, TextField, Button, CircularProgress, Alert, MenuItem, IconButton } from '@mui/material';
import CategoryCreationDialog from './CategoryCreationDialog';
import EditIcon from '@mui/icons-material/esm/Edit';

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
    const [categoryToUpdate, setCategoryToUpdate] = useState(null);

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

                if (userId) {
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
        setCategoryToUpdate(null);
        setCategoryDialogOpen(true);
    };

    const handleOpenUpdateDialog = (category) => {
        setCategoryToUpdate(category);
        setCategoryDialogOpen(true);
    };

    const handleCloseCategoryDialog = () => {
        setCategoryDialogOpen(false);
    };

    const handleCategoryUpdated = (updatedCategory) => {
        setCategories(
            categories.map((category) => (category.categoryId === updatedCategory.categoryId ? updatedCategory : category))
        );
        if (formValues.category === updatedCategory.categoryId) {
            setFormValues({ ...formValues, category: updatedCategory.categoryId });
        }
    };

    const handleCategoryCreated = (newCategory) => {
        setCategories([...categories, newCategory]);
        setFormValues({ ...formValues, category: newCategory.categoryId });
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
                                    <MenuItem key={category.categoryId} value={category.categoryId}>
                                        {category.name}
                                        <IconButton onClick={() => handleOpenUpdateDialog(category)} size="small" sx={{ ml: 2 }}>
                                            <EditIcon fontSize="small" />
                                        </IconButton>
                                    </MenuItem>
                                ))}
                            </TextField>
                            <Box sx={{ display: 'flex', justifyContent: 'space-between', mt: 2 }}>
                                <Button variant="contained" onClick={handleOpenCategoryDialog}>
                                    Create New Category
                                </Button>
                                <Button type="submit" variant="contained" color="primary">
                                    Update Habit
                                </Button>
                            </Box>
                        </form>
                    </>
                )}
                <CategoryCreationDialog
                    open={categoryDialogOpen}
                    onClose={handleCloseCategoryDialog}
                    onCategoryCreated={handleCategoryCreated}
                    onCategoryUpdated={handleCategoryUpdated}
                    categoryToUpdate={categoryToUpdate}
                />
            </Box>
        </Modal>
    );
};

export default UpdateHabitForm;
