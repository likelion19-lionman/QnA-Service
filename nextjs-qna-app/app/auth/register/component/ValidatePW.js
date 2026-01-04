'use client'

import { useState, useEffect } from 'react';

const PASSWORD_REGEX = /^(?=.*[!@#$%^&*()_+\-={}[\]:;"'<>,.?/]).{8,}$/;


export default function ValidatePW({
    password,
    setPassword,
    setPasswordValid
}) {

    const [passwordChecked, setPasswordChecked] = useState(false);

    const [confirmPassword, setConfirmPassword] = useState("");
    const [confirmChecked, setConfirmChecked] = useState(false);

    useEffect(() => {
        setPasswordChecked(PASSWORD_REGEX.test(password));
    }, [password]);

    useEffect(() => {
        setConfirmChecked(password === confirmPassword);
    }, [password, confirmPassword]);

    useEffect(() => {
        setPasswordValid(passwordChecked && confirmChecked)
    }, [passwordChecked && confirmChecked]);

    return (
        <div className="space-y-3">
            <label className="block text-sm font-medium text-slate-700">비밀번호</label>
            <input 
                type="password"
                placeholder="비밀번호를 입력하세요"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                className="w-full px-4 py-2 border border-slate-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent transition-all"
            />
            {!passwordChecked && (
                <div className="text-sm text-red-600 bg-red-50 border border-red-200 rounded-lg px-3 py-2">
                    {'비밀번호는 비어 있으면 안되고 8자 이상 및 특수문자 1개 이상이 포함되어야 합니다.'}
                </div>
            )}
            {passwordChecked && (
                <div className="text-sm text-emerald-600 bg-emerald-50 border border-emerald-200 rounded-lg px-3 py-2">
                    {'안전한 비밀번호 입니다.'}
                </div>
            )}
            
            <div className="space-y-2">
                <input
                    type="password"
                    placeholder="비밀번호 확인"
                    value={confirmPassword}
                    onChange={(e) => setConfirmPassword(e.target.value)}
                    className="w-full px-4 py-2 border border-slate-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent transition-all"
                />
            </div>

            {!confirmChecked && (
                <div className="text-sm text-red-600 bg-red-50 border border-red-200 rounded-lg px-3 py-2">
                    비밀번호가 일치하지 않습니다.
                </div>
            )}

            {confirmChecked && (
                <div className="text-sm text-emerald-600 bg-emerald-50 border border-emerald-200 rounded-lg px-3 py-2">
                    비밀번호가 일치합니다.
                </div>
            )}
        </div>
    )

}
    