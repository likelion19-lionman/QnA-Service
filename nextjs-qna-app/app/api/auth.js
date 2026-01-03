import { baseRequest, getBaseURL } from "./api";

export async function checkDuplication(username) {
  const API_BASE_URL = getBaseURL();

  const res = await fetch(`${API_BASE_URL}/auth/check-duplication`, {
    method: "POST",
    headers: {
      "Content-Type": "text/plain",
      Accept: "application/json",
    },
    body: username,
  });

  if (!res.ok) throw new Error((await res.text()) || "아이디 중복 확인 실패");

  return res.ok;
}

export async function register(username, isEmailVerified, email, password) {
  const API_BASE_URL = getBaseURL();

  const res = await fetch(`${API_BASE_URL}/auth/register`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Accept: "text/plain",
    },
    body: JSON.stringify({
      username: username,
      isEmailVerified: isEmailVerified,
      email: email,
      password: password,
    }),
    credentials: "include",
  });

  // 에러 응답 처리
  if (!res.ok) {
    try {
      const errorData = await res.json();
      throw new Error(errorData.message || "회원가입에 실패하였습니다.");
    } catch (parseError) {
      throw new Error("회원가입에 실패하였습니다.");
    }
  }

  const refreshToken = await res.text();

  if (refreshToken) {
    console.log("✅ 회원가입 완료 및 refreshToken 저장");
    localStorage.setItem("refreshToken", refreshToken);
    return true;
  }

  return false;
}

export async function login(username, password) {
  const res = await baseRequest(
    "/auth/login",
    "POST",
    {
      "Content-Type": "application/json",
      Accept: "text/plain",
    },
    {
      username: username,
      password: password,
    },
    `${username} 로그인 실패`
  );

  if (res) {
    console.log("✅ 로그인 완료 및 refreshToken -> LocalStorage 저장");
    localStorage.setItem("refreshToken", res);
  }
}

export async function requestAuthCode(email) {
  const API_BASE_URL = getBaseURL();

  const res = await fetch(`${API_BASE_URL}/auth/email/send`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Accept: "application/json",
    },
    body: JSON.stringify({ email }),
    credentials: "include",
  });

  const data = await res.json();

  if (!res.ok) {
    throw new Error(data.message || "이메일 전송 요청 실패");
  }

  return data;
}

export async function validateAuthCode(email, code) {
  const API_BASE_URL = getBaseURL();

  const res = await fetch(`${API_BASE_URL}/auth/email/verify`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Accept: "application/json",
    },
    body: JSON.stringify({
      email: email,
      authCode: code,
    }),
    credentials: "include",
  });

  const data = await res.json();

  if (!data) {
    throw new Error("인증 코드가 일치하지 않습니다.");
  }

  return "인증번호가 일치합니다.";
}

export async function logout() {
  const refreshToken = localStorage.getItem("refreshToken");

  if (!refreshToken) {
    alert("이미 로그아웃 상태입니다.");
    window.location.href = "/";
    return;
  }

  localStorage.removeItem("refreshToken");

  await baseRequest(
    "/auth/logout",
    "POST",
    {
      "Content-Type": "application/json",
    },
    {
      refreshToken: refreshToken,
    },
    "로그아웃 실패"
  );
}
