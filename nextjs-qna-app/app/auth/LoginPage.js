'use client'
import { useState } from "react";
import { login } from "../api/auth";

export default function LoginPage() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    
    async function submit(e)  {
        e.prevenDefault();
        try{
            await login(username, password);
            alert('로그인 성공')
        }catch(e){
            alert(e.message);
        }
    };
        
    

    return (
        <div>
            <form onSubmit={submit}>
                <input
                    placeholder="username"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                />

            
                <input
                    type="password"
                    placeholder="password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                />
            
                <button type="submit">login</button>
            </form>
        </div>
        )


}