'use client'

import { useState } from "react";
import { login } from "@/app/api/auth";
import { useRouter } from "next/navigation";

export default function LoginPage() {
    const router = useRouter();

    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    async function submit(e) {
        e.preventDefault();  // 수정!
        console.log("로그인 실행중...");
        
        try {
            const res = await login(username, password);
            console.log(res);
            alert('로그인 성공');
            router.push("/");  // 메인으로 이동
        } catch (e) {
            alert(e.message);
        }
    }

    return (
        <div>
            <form onSubmit={submit}>  {/* 이렇게 하는 게 더 깔끔 */}
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
            
                <button type="submit">
                    login
                </button>
            </form>
            
            <button
                type="button"
                onClick={() => router.push("/auth/register")}
            >
                회원가입
            </button>
        </div>
    );
}