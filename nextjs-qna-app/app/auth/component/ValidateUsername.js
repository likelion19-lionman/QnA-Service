'use client'
import { useMemo } from "react";
import { checkDuplication } from "@/app/api/auth.js"


const USERNAME_REGEX = /^[a-zA-Z0-9]{4,}$/;
// a -z + A-Z + 0~9 -> 4글자 이상

 const validate =(username) => {
        if(!USERNAME_REGEX.test(username)){
            return{ok: false, message:'아이디는 영문/숫자만 입력해주십시오'}
        }
        return{ok: true, message: ''}
    }

export default function ValidateUsername({username, setUsername, onChecked}) {

    const usernameRule = useMemo(() => validate(username), [username])

    const checkUsername = async () => {
        if (!usernameRule.ok) return;

        try {
            const available = await checkDuplication(username);

            if (available) {
                alert('가입 가능한 아이디입니다.');
                onChecked?.(true);
            } else {
                alert('이미 존재하는 아이디입니다.');
                onChecked?.(false);
            }
        } catch (e) {
            alert(e.message ?? '중복 확인 실패');
            onChecked?.(false);
        }
     };


    return(
    <div>
        <input
            placeholder="username"
            value={username}
            onChange={(e) => {
                setUsername(e.target.value)
                onChecked?.(false)
            }}
        />
            {username.length >0 && username.length < 4 && (
                <div>아이디는 최소 4글자 이상이어야 합니다.</div>
                )}

            {username.length >0 && !usernameRule.ok && (
                <div>{usernameRule.message}</div>
                )}

        <button type="button" onClick={checkUsername}>중복체크</button>
    </div>
    )
}