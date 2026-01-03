'use client'

import { useState } from "react"
import { register } from "@/app/api/auth"
import EmailAuth from "@/app/auth/register/component/EmailAuth";
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
        <div className="min-h-screen flex items-center justify-center px-4 bg-slate-50 py-8">
            <div className="w-full max-w-md">
                <div className="bg-white rounded-lg shadow-sm border border-slate-200 p-8 space-y-6">
                    <div className="text-center space-y-2">
                        <h1 className="text-3xl font-bold text-slate-800">회원가입</h1>
                        <p className="text-slate-600 text-sm">새 계정을 만드세요</p>
                    </div>
                    
                    <form onSubmit={submit} className="space-y-6">
                        <ValidateUsername 
                            username={username} 
                            setUsername={setUsername} 
                            onChecked={setUsernameVerified}
                        />

                        <EmailAuth
                            email={email}
                            setEmail={setEmail}
                            onVerified={setEmailVerified}
                        />

                        <ValidatePW 
                            password={password}
                            setPassword={setPassword}
                            confirm={confirm}
                            setConfirm={setConfirm}
                        />
                        
                        <button 
                            type="submit"
                            className="w-full px-4 py-3 bg-indigo-600 text-white rounded-lg font-medium hover:bg-indigo-700 transition-colors duration-200 shadow-sm"
                        >
                            회원가입
                        </button>
                    </form>
                </div>
            </div>
        </div>
    )
}