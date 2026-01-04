'use client'

import { useState } from "react";
import { login } from "@/app/api/auth";
import { useRouter } from "next/navigation";

export default function LoginPage() {
    const router = useRouter();

    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    async function submit(e) {
        e.preventDefault();
        
        try {
            const isLogined = await login(username, password);

            if (isLogined) {
                alert('로그인 성공');
                router.push("/");  // 메인으로 이동
            } else {
                alert('로그인 실패');
            }
        } catch (e) {
            alert(e.message);
        }
    }

    return (
        <div className="min-h-screen flex items-center justify-center px-4 bg-slate-50">
            <div className="w-full max-w-md">
                <div className="bg-white rounded-lg shadow-sm border border-slate-200 p-8 space-y-6">
                    <div className="text-center space-y-2">
                        <h1 className="text-3xl font-bold text-slate-800">로그인</h1>
                        <p className="text-slate-600 text-sm">계정에 로그인하세요</p>
                    </div>
                    
                    <form onSubmit={submit} className="space-y-4">
                        <div className="space-y-2">
                            <input
                                placeholder="사용자명"
                                value={username}
                                onChange={(e) => setUsername(e.target.value)}
                                className="w-full px-4 py-3 border border-slate-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent transition-all"
                            />
                        </div>
                    
                        <div className="space-y-2">
                            <input
                                type="password"
                                placeholder="비밀번호"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                className="w-full px-4 py-3 border border-slate-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent transition-all"
                            />
                        </div>
                    
                        <button 
                            type="submit"
                            className="w-full px-4 py-3 bg-indigo-600 text-white rounded-lg font-medium hover:bg-indigo-700 transition-colors duration-200 shadow-sm"
                        >
                            로그인
                        </button>
                    </form>
                    
                    <div className="pt-4 border-t border-slate-200">
                        <button
                            type="button"
                            onClick={() => router.push("/auth/register")}
                            className="w-full px-4 py-3 bg-slate-100 text-slate-700 rounded-lg font-medium hover:bg-slate-200 transition-colors duration-200"
                        >
                            회원가입
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
}