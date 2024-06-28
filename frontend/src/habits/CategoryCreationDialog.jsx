import React, { useState, useEffect } from 'react';
import { Dialog, DialogActions, DialogContent, DialogTitle, TextField, Button } from '@mui/material';
import axios from 'axios';

const CategoryCreationDialog = ({ open, onClose, onCategoryCreated, onCategoryUpdated, categoryToUpdate }) => {
    const [category, setCategory] = useState({ name: '' });

    useEffect(() => {
        if (categoryToUpdate) {
            setCategory({ name: categoryToUpdate.name });
        } else {
            setCategory({ name: '' });
        }
    }, [categoryToUpdate]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setCategory((prevCategory) => ({
            ...prevCategory,
            [name]: value,
        }));
    };

    const handleSubmit = async () => {
        const jwt = localStorage.getItem('jwt');
        try {
            if (categoryToUpdate) {
                //console.log(categoryToUpdate.categoryId);
                const response = await axios.put(
                    `http://localhost:8080/api/v1/habit-category/${categoryToUpdate.categoryId}`,
                    { ...category },
                    {
                        headers: { Authorization: `Bearer ${jwt}` },
                    }
                );
                onCategoryUpdated(response.data);
            } else {
                const response = await axios.post(
                    'http://localhost:8080/api/v1/habit-category/',
                    { ...category },
                    {
                        headers: { Authorization: `Bearer ${jwt}` },
                    }
                );
                onCategoryCreated(response.data);
            }
            onClose();
        } catch (error) {
            console.error('Error saving category:', error);
        }
    };

    return (
        <Dialog open={open} onClose={onClose}>
            <DialogTitle>{categoryToUpdate ? 'Update Category' : 'Create New Category'}</DialogTitle>
            <DialogContent>
                <TextField
                    autoFocus
                    margin="dense"
                    name="name"
                    label="Category Name"
                    type="text"
                    fullWidth
                    value={category.name}
                    onChange={handleChange}
                />
            </DialogContent>
            <DialogActions>
                <Button onClick={onClose} color="primary">
                    Cancel
                </Button>
                <Button onClick={handleSubmit} color="primary">
                    {categoryToUpdate ? 'Update' : 'Create'}
                </Button>
            </DialogActions>
        </Dialog>
    );
};

export default CategoryCreationDialog;
