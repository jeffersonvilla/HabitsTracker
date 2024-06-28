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

const validationSchema = Yup.object().shape({
    username: Yup.string().required('Username is required'),
    email: Yup.string().email('Invalid email format').required('Email is required'),
    password: Yup.string()
        .min(6, 'Password must be at least 6 characters')
        .matches(
            /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\S+$).{8,}$/,
            'Password must contain at least one lowercase letter, one uppercase letter, one digit, and one special character'
        )
        .required('Password is required'),
});

const RegisterForm = () => {
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
            const response = await fetch('http://localhost:8080/api/v1/auth/register', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data),
            });


            if (response.status === 200) {
                const responseData = await response.text(); // Assuming a plain text response
                //console.log('Registration successful!', responseData);
                setSnackbarMessage(responseData);
                setSnackbarOpen(true);
                reset();
                // Handle successful registration (e.g., redirect to login page)
            } else if (response.status === 400) {
                const responseData = await response.json(); // Assuming a JSON response
                throw new Error(responseData.message || 'Registration failed');
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
                        Register
                    </Typography>
                    <form onSubmit={handleSubmit(onSubmit)} style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
                        <TextField
                            {...register('username')}
                            label="Username"
                            error={!!errors.username}
                            helperText={errors.username?.message}
                        />
                        <TextField
                            {...register('email')}
                            label="Email"
                            type="email"
                            error={!!errors.email}
                            helperText={errors.email?.message}
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
                            {isLoading ? 'Registering...' : 'Register'}
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

export default RegisterForm;
