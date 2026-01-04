const getBaseURL = () => {
    if (typeof window !== 'undefined') {
        // 클라이언트 사이드
        return `${window.location.protocol}//${window.location.hostname}/api`;
    }
    // 서버 사이드
    return 'http://back/api';
};

const API_BASE_URL = getBaseURL();

export async function baseRequest(url, method, headers, body, errMsg) {

    const fetchOptions = {
        method: method,
        headers: headers,
        body: body,
        credentials: 'include', // 쿠키에 있는 것까지 전송
    };

    let res = await fetch(`${API_BASE_URL}${url}`, fetchOptions);

    // 인증 예외 코드 401
    if (res.status === 401) {
        console.log("이거 호출됨");
        await refresh();
        res = await fetch(`${API_BASE_URL}${url}`, fetchOptions);
    }

    if (!res.ok) {
        const errorData = await res.json();
        if (errorData.detail)
            throw new Error(errorData.detail);
        else {
            const errorMessage = Object.values(errorData).join(".\n");
            throw new Error(errorMessage || "유효성 검사에 실패했습니다.");
        }
    }
    
    // 204 No Content
    if (res.status === 204)
        return true

    // produces = text/plain 은 따로 처리
    if (headers.Accept && headers.Accept === "text/plain") {
        const text = await res.text();
        return text;
    }

    const data = await res.json();
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
