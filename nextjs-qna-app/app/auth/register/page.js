"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { register } from "@/app/api/auth";
import EmailAuth from "@/app/auth/register/component/EmailAuth";
import ValidatePW from "./component/ValidatePW";
import ValidateUsername from "./component/ValidateUsername";

export default function RegisterPage() {
  const router = useRouter();

  const [username, setUsername] = useState("");
  const [usernameVerified, setUsernameVerified] = useState(false);

  const [email, setEmail] = useState("");
  const [emailVerified, setEmailVerified] = useState(false);

  const [password, setPassword] = useState("");
  const [confirm, setConfirm] = useState("");

  const submit = async (e) => {
    e.preventDefault();
    if (!usernameVerified) return alert("아이디 중복확인을 해주세요.");
    if (!emailVerified) return alert("이메일 인증을 완료해주세요.");
    if (!password || password.trim().length === 0)
      return alert("비밀번호를 입력해주세요.");
    if (!confirm || confirm.trim().length === 0)
      return alert("비밀번호 확인을 입력해주세요.");
    if (password !== confirm) return alert("비밀번호가 일치하지 않습니다.");

    try {
      const res = await register(username, email, password);

      if (res) {
        alert("회원가입이 완료되었습니다.");
        router.push("/");
      }
    } catch (e) {
      alert(e.message || "회원가입에 실패하였습니다.");
    }
  };

  const goHome = () => {
    router.push("/");
  };

  return (
    <form onSubmit={submit}>
      <ValidateUsername
        username={username}
        setUsername={setUsername}
        onChecked={setUsernameVerified}
      />

      <EmailAuth
        email={email}
        setEmail={setEmail}
        onVerified={setEmailVerified}
      />

      <ValidatePW
        password={password}
        setPassword={setPassword}
        confirm={confirm}
        setConfirm={setConfirm}
      />

      <div>
        <button type="submit">회원가입</button>
        <button type="button" onClick={goHome}>
          홈으로
        </button>
      </div>
    </form>
  );
}
