'use client'

import { useState } from "react"
import { requestAuthCode, validateAuthCode } from "@/lib/api"

export default function EmailAuth({email, setEmail, onVerified}){
    
    const [code, setCode] = useState('');
    const [status, setStatus] = useState('');
    const [message, setMessage] = useState('idle');


    const sendCode = async () => {
        setMessage('');
        try{
            await requestAuthCode(email);
            setStatus('sent')
            setMessage('인증 코드가 전송되었습니다')
        }catch (e){
            setStatus('error')
            setMessage(e.message)
        }
    };

    const verifyCode = async () => {
        setMessage('');
        try{
            await validateAuthCode(email, code);
            setStatus('verified');
            setMessage('이메일 인증 완료');
            onVerified(true); 
        }catch(e) {
            setStatus('sent');
            setMessage(e.message)
        }
    };

    return (
        <div>
            <input
                placeholder="email"    
                value={email}
                onChange={(e) => {
                    setEmail(e.target.value)
                    setStatus('idle')
                    onVerified(false);
                }}
            />
            <button 
                type="button" 
                onClick={sendCode} 
                disabled={!email}>
                    인증코드 보내기
            </button>
                
            {status === 'sent' && (
                <>
                <input
                    placeholder="auth code"
                    value={code}
                    onChange={(e) => setCode(e.target.value)} />
                    
                <button type="button" onClick={verifyCode}>
                    인증 확인
                </button>
                
                {message && <div>{message}</div>}
                </>
            )}
        </div>
    )
}