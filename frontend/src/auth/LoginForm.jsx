import React, { useState } from 'react';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import * as Yup from 'yup';
import {
    TextField,
    Button,
    FormControl,
    InputLabel,
    OutlinedInput,
    FormHelperText,
    Card,
    CardContent,
    Typography,
    Snackbar,
    Alert,
} from '@mui/material';

// Custom validation schema to handle either username or email
const validationSchema = Yup.object().shape({
    usernameOrEmail: Yup.string().required('Username or Email is required'),
    password: Yup.string().required('Password is required'),
});

const LoginForm = () => {
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState(null);
    const [snackbarOpen, setSnackbarOpen] = useState(false);
    const [snackbarMessage, setSnackbarMessage] = useState('');

    const { register, handleSubmit, reset, formState: { errors } } = useForm({
        resolver: yupResolver(validationSchema),
    });

    const handleCloseSnackbar = () => {
        setSnackbarOpen(false);
    };

    const onSubmit = async (data) => {
        setIsLoading(true);
        setError(null);

        try {
            const response = await fetch('http://localhost:8080/api/v1/auth/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ username: data.usernameOrEmail, email: data.usernameOrEmail, password: data.password }),
            });

            if (response.status === 200) {
                const jwt = await response.text(); 
                console.log('Login successful!', jwt);
                setSnackbarMessage('Login successful!');
                setSnackbarOpen(true);
                reset();
                // Handle successful login (save JWT, redirect to dashboard)
            } else if (response.status === 400) {
                const responseData = await response.json(); 
                throw new Error(responseData.message || 'Login failed');
            } else if (response.status === 403) {
                throw new Error('Please verify your account by clicking the link sent to your email.');
            } else {
                throw new Error('Unexpected error occurred');
            }  

        } catch (error) {
            setError(error.message);
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh' }}>
            <Card variant="outlined">
                <CardContent>
                    <Typography variant="h5" component="div" gutterBottom>
                        Login
                    </Typography>
                    <form onSubmit={handleSubmit(onSubmit)} style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
                        <TextField
                            {...register('usernameOrEmail')}
                            label="Username or Email"
                            error={!!errors.usernameOrEmail}
                            helperText={errors.usernameOrEmail?.message}
                        />
                        <FormControl fullWidth variant="outlined">
                            <InputLabel htmlFor="password">Password</InputLabel>
                            <OutlinedInput
                                {...register('password')}
                                type="password"
                                id="password"
                                error={!!errors.password}
                                label="Password"
                            />
                            <FormHelperText error={!!errors.password}>
                                {errors.password?.message}
                            </FormHelperText>
                        </FormControl>
                        <Button type="submit" variant="contained" disabled={isLoading}>
                            {isLoading ? 'Logging in...' : 'Login'}
                        </Button>
                        {error && <p style={{ color: 'red' }}>{error}</p>}
                    </form>
                </CardContent>
            </Card>
            <Snackbar
                open={snackbarOpen}
                autoHideDuration={6000}
                onClose={handleCloseSnackbar}
                anchorOrigin={{ vertical: 'bottom', horizontal: 'center' }}
            >
                <Alert onClose={handleCloseSnackbar} severity="success" sx={{ width: '100%' }}>
                    {snackbarMessage}
                </Alert>
            </Snackbar>
        </div>
    );
};

export default LoginForm;
