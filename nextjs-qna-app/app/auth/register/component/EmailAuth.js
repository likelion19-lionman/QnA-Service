"use client";

import { useMemo, useState } from "react";
import { requestAuthCode, validateAuthCode } from "@/app/api/auth";

const GMAIL_REGEX = /^[A-Za-z0-9._%+-]+@gmail\.com$/;

const validate = (email) => {
  if (!GMAIL_REGEX.test(email)) {
    return { ok: false, message: "Gmail 계정만 사용할 수 있습니다." };
  }
  return { ok: true, message: "" };
};

export default function EmailAuth({ email, setEmail, onVerified }) {
  const [code, setCode] = useState("");
  const [status, setStatus] = useState("");
  const [message, setMessage] = useState("idle");

  const emailRule = useMemo(() => validate(email), [email]);

  const sendCode = async () => {
    setMessage("");

    if (!emailRule.ok) return alert(emailRule.message);

    try {
      await requestAuthCode(email);
      setStatus("sent");
      setMessage("인증 코드가 전송되었습니다");
    } catch (e) {
      setStatus("error");
      setMessage(e.message);
    }
  };

  const verifyCode = async () => {
    setMessage("");
    try {
      await validateAuthCode(email, code);
      setStatus("verified");
      setMessage("이메일 인증 완료");
      onVerified(true);
    } catch (e) {
      setStatus("sent");
      setMessage(e.message);
    }
  };

  return (
    <div>
      <input
        placeholder="email"
        value={email}
        onChange={(e) => {
          setEmail(e.target.value);
          setStatus("idle");
          onVerified(false);
        }}
      />
      <button type="button" onClick={sendCode} disabled={!email}>
        {status === "sent" ? "재전송" : "인증 코드 전송"}
      </button>

      <input
        placeholder="auth code"
        value={code}
        onChange={(e) => setCode(e.target.value)}
        disabled={status !== "sent"} // 코드 전송 전에는 비활성화
      />
      <button
        type="button"
        onClick={verifyCode}
        disabled={status !== "sent" || !code}
      >
        인증 확인
      </button>

      {message && <div>{message}</div>}
    </div>
  );
}
