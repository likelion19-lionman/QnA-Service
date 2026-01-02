import { baseRequest } from "./api";

export async function checkDuplication(username) {
  return await baseRequest(
    "/auth/check-duplication",
    "POST",
    {
      "Content-Type": "text/plain",
      Accept: "application/json",
    },
    {
      username: username,
    }
  );
}

export async function register(username, email, password) {
  const res = await baseRequest(
    "/auth/register",
    "POST",
    {
      "Content-Type": "application/json",
      Accept: "application/json",
    },
    {
      username: username,
      email: email,
      password: password,
    },
    `회원가입 불가`
  );

  if (res && res.refreshToken) {
    console.log("✅ 회원가입 완료 및 refreshToken 저장");
    localStorage.setItem("refreshToken", res.refreshToken);
  }
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
  return await baseRequest(
    "/auth/email/send",
    "POST",
    {
      "Content-Type": "application/json",
      Accept: "application/json",
    },
    {
      email: email,
    },
    `${email} 이메일 전송 요청 실패`
  );
}

export async function validateAuthCode(email, code) {
  return await baseRequest(
    "/auth/email/verify",
    "POST",
    {
      "Content-Type": "application/json",
      Accept: "application/json",
    },
    {
      email: email,
      code: code,
    },
    `${email} 이메일 인증 실패`
  );
}

export async function logout() {
  const refreshToken = localStorage.getItem("refreshToken");

  // refreshToken이 없으면 이미 로그아웃 상태
  if (!refreshToken) {
    console.log("이미 로그아웃 상태입니다.");
    window.location.href = "/auth/login";
    return;
  }

  localStorage.removeItem("refreshToken");

  // refreshToken이 있으면 로그아웃 요청
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

  console.log("✅ 로그아웃 완료 및 refreshToken -> LocalStorage 제거");
}
