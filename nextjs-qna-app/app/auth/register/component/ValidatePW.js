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
        <div>
            <input 
                type="password"
                placeholder="password"
                value={password}
                onChange={(e) => {
                    setPassword(e.target.value)
                    setChecked(false)
                }} 
                />
                {!pwRule.ok && password.length > 0 && <div>{pwRule.message}</div>}
                <br/>
            <input
                type="password"
                placeholder="confirmPW"
                value={confirm}
                onChange={(e) => {
                    setConfirm(e.target.value),
                    setChecked(false)
                }}
            />
            <button type="button" onClick={() => setChecked(true)}>비밀번호 확인</button>

            {checked && !matchOk && (
                <div>비밀번호가 일치하지 않습니다.</div>
                )}

            {checked && matchOk && (
                <div>비밀번호가 일치합니다.</div>
            )}
        </div>
    )

}
    