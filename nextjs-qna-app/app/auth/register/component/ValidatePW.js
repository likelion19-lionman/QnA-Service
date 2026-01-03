'use client'

import { useState, useMemo } from "react";

const PASSWORD_REGEX =
  /^(?=.*[!@#$%^&*()_+\-={}[\]:;"'<>,.?/]).{8,}$/;

function validate(pw) {
        if (!PASSWORD_REGEX.test(pw))
            return { ok: false , message: '비밀번호는 8자 이상, 특수문자는 1개 이상 포함되어야 합니다.'}
    
    return {ok:true, message:'안전한 비밀번호입니다.'}
    }

export default function ValidatePW({ password, setPassword, confirm, setConfirm }) {
  
    const [checked, setChecked] = useState(false);

    const pwRule = useMemo(() => validate(password), [password])
    
    const matchOk = confirm.length > 0 && password === confirm;

    return (
        <div className="space-y-3">
            <label className="block text-sm font-medium text-slate-700">비밀번호</label>
            <input 
                type="password"
                placeholder="비밀번호를 입력하세요"
                value={password}
                onChange={(e) => {
                    setPassword(e.target.value)
                    setChecked(false)
                }}
                className="w-full px-4 py-2 border border-slate-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent transition-all"
            />
            {!pwRule.ok && password.length > 0 && (
                <div className="text-sm text-red-600 bg-red-50 border border-red-200 rounded-lg px-3 py-2">
                    {pwRule.message}
                </div>
            )}
            {pwRule.ok && password.length > 0 && (
                <div className="text-sm text-emerald-600 bg-emerald-50 border border-emerald-200 rounded-lg px-3 py-2">
                    {pwRule.message}
                </div>
            )}
            
            <div className="space-y-2">
                <input
                    type="password"
                    placeholder="비밀번호 확인"
                    value={confirm}
                    onChange={(e) => {
                        setConfirm(e.target.value)
                        setChecked(false)
                    }}
                    className="w-full px-4 py-2 border border-slate-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent transition-all"
                />
                <button 
                    type="button" 
                    onClick={() => setChecked(true)}
                    className="w-full px-4 py-2 bg-slate-100 text-slate-700 rounded-lg font-medium hover:bg-slate-200 transition-colors duration-200"
                >
                    비밀번호 확인
                </button>
            </div>

            {checked && !matchOk && (
                <div className="text-sm text-red-600 bg-red-50 border border-red-200 rounded-lg px-3 py-2">
                    비밀번호가 일치하지 않습니다.
                </div>
            )}

            {checked && matchOk && (
                <div className="text-sm text-emerald-600 bg-emerald-50 border border-emerald-200 rounded-lg px-3 py-2">
                    비밀번호가 일치합니다.
                </div>
            )}
        </div>
    )

}
    