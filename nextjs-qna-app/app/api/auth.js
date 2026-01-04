import { baseRequest } from "./api";

// 확인 끝
export async function checkDuplication(username) {
    const res = await baseRequest(
        '/auth/check-duplication',
        'POST',
        {
          'Content-Type': 'text/plain',
          'Accept': 'application/json'
        },
        username,
        'check-duplication 페치 오류'
    );

    return res;
}

// 확인 끝
export async function requestAuthCode(email) {
    return await baseRequest(
        '/auth/email/send',
        'POST',
        {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        },
        JSON.stringify({
            email: email
        }),
        '이메일 전송 실패'
    );
}


// 확인 끝
export async function validateAuthCode(email, code) {
    const res = baseRequest(
        '/auth/email/verify',
        'POST',
        {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        },
        JSON.stringify({
           email: email,
           authCode: code
        })
    );

    return res;
}

// 확인 끝
export async function logout() {
  const refreshToken = localStorage.getItem("refreshToken");

  if (!refreshToken) {
    alert("이미 로그아웃 상태입니다.");
    window.location.href = "/";
    return;
  }

  localStorage.removeItem("refreshToken");

  await baseRequest(
    '/auth/logout',
    'POST',
    {
        'Content-Type': 'application/json',
    },
    JSON.stringify({
        refreshToken: refreshToken,
    }),
    '로그아웃 실패'
  );
}

// 
export async function register(username, isEmailVerified, email, password) {
    const refreshToken = await baseRequest(
        '/auth/register',
        'POST',
        {
            'Content-Type': 'application/json',
            'Accept': 'text/plain'
        },
        JSON.stringify({
            username: username,
            isEmailVerified: isEmailVerified,
            email: email,
            password: password,
        }),
        '회원가입 불가'
    );

    if (refreshToken) {
        localStorage.setItem('refreshToken', refreshToken);
        return true;
    }
    return false;
}

// 확인 끝
export async function login(username, password) {
    const refreshToken = await baseRequest(
        '/auth/login',
        'POST',
        {
          'Content-Type': 'application/json',
          'Accept': 'text/plain',
        },
        JSON.stringify({
            username: username,
            password: password,
        }),
        `${username} 로그인 실패`
    );

    if (refreshToken) {
        localStorage.setItem('refreshToken', refreshToken);
        return true;
    }
    
    return false;
}
