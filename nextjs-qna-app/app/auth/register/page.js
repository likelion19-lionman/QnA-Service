'use client'

import { useState } from "react"
import { register } from "../api/auth"
import EmailAuth from "./component/EmailAuth";
import ValidatePW from "./component/ValidatePW";
import ValidateUsername from "./component/ValidateUsername";

export default function RegisterPage() {
    const [username, setUsername] = useState('');
    const [usernameVerified, setUsernameVerified] = useState(false)

    const [email, setEmail] = useState('');
    const [emailVerified, setEmailVerified] = useState(false);

    const [password, setPassword] = useState('');
    const [confirm, setConfirm] = useState('');

   const submit = async (e) => {
        e.preventDefault();
        if (!usernameVerified) return alert('아이디 중복확인을 해주세요.');
        if (!emailVerified) return alert('이메일 인증을 완료해주세요.');
        await register(username, email, password);
    };

    return (
        <form onSubmit={submit}>
            <ValidateUsername 
                username={username} 
                setUsername={setUsername} 
                onChecked={setUsernameVerified}
            />

            <EmailAuth
                email = {email}
                setEmail={setEmail}
                onVerified={setEmailVerified}
            />

            <ValidatePW 
                password={password}
                setPassword={setPassword}
                confirm={confirm}
                setConfirm={setConfirm}
            />
            <button type="submit">회원가입</button>
        </form>

    )
}