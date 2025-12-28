import { baseRequest } from './api';

export async function checkDuplication(
    username
) {
    return await baseRequest(
        '/auth/check-duplication',
        'POST',
        {
            'Content-Type': 'text/plain',
            'Accept': 'application/json'
        },
        {
            username: username
        }
    )
}

export async function register(
    username,
    email,
    password
) {
    return await baseRequest(
        '/auth/register',
        'POST',
        {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        },
        {
            username: username,
            email: email,
            password: password
        },
        `회원가입 불가`
    );
}

export async function login(username, password) {
    return await baseRequest(
        '/auth/login',
        'POST',
        {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        },
        {
            'username': username,
            'password': password
        },
        `${username} 로그인 실패`
    );
}

export async function requestAuthCode(email) {
    return await baseRequest(
        '/auth/email/send',
        'POST',
        {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        },
        {
            'email': email
        },
        `${email} 이메일 전송 요청 실패`
    );
}

export async function validateAuthCode(email, code) {
    return await baseRequest(
        '/auth/email/verify',
        'POST',
        {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        },
        {
            'email': email,
            'code': code
        },
        `${email} 이메일 인증 실패`
    );
}