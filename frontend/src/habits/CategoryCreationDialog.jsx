import React, { useState } from 'react';
import { Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle, TextField, Button, Alert } from '@mui/material';
import axios from 'axios';

const CategoryCreationDialog = ({ open, onClose, onCategoryCreated }) => {
  const [categoryName, setCategoryName] = useState('');
  const [error, setError] = useState(null);

  const handleCategoryNameChange = (e) => {
    setCategoryName(e.target.value);
  };

  const handleCreateCategory = async () => {
    const jwt = localStorage.getItem('jwt');
    if (!jwt) {
      setError("JWT token not found");
      return;
    }

    try {
      const response = await axios.post('http://localhost:8080/api/v1/habit-category/', { name: categoryName }, {
        headers: { Authorization: `Bearer ${jwt}` },
      });
      onCategoryCreated(response.data);
      onClose();
    } catch (error) {
      setError(error.response?.data?.message || "Error creating category");
    }
  };

  return (
    <Dialog open={open} onClose={onClose}>
      <DialogTitle>Create New Category</DialogTitle>
      <DialogContent>
        <DialogContentText>
          Please enter the name of the new category.
        </DialogContentText>
        {error && <Alert severity="error">{error}</Alert>}
        <TextField
          autoFocus
          margin="dense"
          label="Category Name"
          type="text"
          fullWidth
          value={categoryName}
          onChange={handleCategoryNameChange}
        />
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose} color="primary">
          Cancel
        </Button>
        <Button onClick={handleCreateCategory} color="primary">
          Create
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default CategoryCreationDialog;
