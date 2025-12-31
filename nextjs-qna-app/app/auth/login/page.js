'use client'
import { useState } from "react";
import { login } from "@/app/api/auth";
import { useRouter } from "next/navigation";

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

    const router = useRouter();
        
    

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
            <button
              type="button"
              onClick={()=> router.push("/auth")}
            >
              회원가입
            </button>
        </div>
        )
}