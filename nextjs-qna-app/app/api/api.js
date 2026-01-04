const getBaseURL = () => {
    if (typeof window !== 'undefined') {
        // 클라이언트 사이드
        return `${window.location.protocol}//${window.location.hostname}/api`;
    }
    // 서버 사이드
    return 'http://back:8080/api';
};

const API_BASE_URL = getBaseURL();

export async function baseRequest(url, method, headers, body, errMsg) {
    const fetchOptions = {
        method: method,
        headers: headers,
        body: body,
        credentials: "include", // 쿠키에 있는 것까지 전송
    };

    let res = await fetch(`${API_BASE_URL}${url}`, fetchOptions);

    // 인증 예외 코드 401
    if (res.status === 401) {
        await refresh();
        res = await fetch(`${API_BASE_URL}${url}`, fetchOptions);
    }

    // 만약 재발급 후에도 ok 가 아니라면 에러 발생
    if (!res.ok) throw new Error(errMsg || "요청 실패");

    // 204 No Content 응답 처리 (로그아웃 시 사용)
    if (res.status === 204) {
        console.log("204 No Content 응답 처리");
        return null;
    }

    if (headers.Accept && headers.Accept === "text/plain") {
        const text = await res.text();
        console.log("⭐️ 결과 (text):", text);
        return text;
    }

    const data = await res.json();
    console.log("⭐️ 결과:", data); // 템플릿 리터럴 대신 , 를 써야 객체 내용이 보임
    return data;
}

async function refresh() {
    const res = await fetch(`${API_BASE_URL}/auth/refresh`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        },
        body: localStorage.getItem('refreshToken'),
        credentials: 'include'
    });

    if (!res.ok) {
        localStorage.removeItem("refreshToken");
        window.location.href = "/auth/login";
        throw new Error("리프레시 토큰이 만료되었습니다.");
    }

    const data = await res.json();
    
    if (data && data.refreshToken)
        localStorage.setItem('refreshToken', data.refreshToken);
}
