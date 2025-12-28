'use client'

import { useState } from "react"
import { register } from "../api/auth"
import EmailAuth from "./component/EmailAuth";
import ValidatePW from "./component/ValidatePW";



export default function RegisterPage(){
    const[username, setUsername] = useState('');

    const [email, setEmail] = useState('');
    const [emailVerified, setEmailVerified] = useState('');

    const [password, setPassword] = useState('');
    const [confirm, setConfirm] = useState('');

   

    

    const submit = async (e) => {
         e.preventDefault();
        if(!emailVerified){
            alert('이메일 인증을 완료해줒세요.')
            return
        }
        await register(username, email, password)
    }


    return (
        <form>
            
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