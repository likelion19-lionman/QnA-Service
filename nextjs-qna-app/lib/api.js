'use client';

 const API_URL_AUTH = 'http://localhost:8080/api/auth' 
 const API_URL_QNA = 'http://localhost:8080/api/qna' 
    

// /api/auth
// Get :: /check-duplication
export async function checkDuplication(username) {
    const response = 
            await fetch(`${API_URL_AUTH}/check-duplication/${encodeURIComponent(username)}`, {
    method: 'GET',
    cache: 'no-store',
});
    if(!response.ok){
    throw new error('중복 확인 실패');
}

    const result = await response.json();
    return result;
}

//중복확인 같은 경우 입력한 정보가 db에서 찾아보게 되는데 get방식으로 하면
//URL에 정보가 노출될 위험이 있음 url?username=user 처럼..
//그래서 이러한 노출을 피하려면 post를 사용하는 것을 권고

// Post :: /register
export async function register(data) {
    const response =await fetch(`${API_URL_AUTH}/register`,{
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(data),
    });

    if(!response.ok){
        throw new Error("회원가입에 실패했습니다.")
    }
    
    return response.json();
    
}

// Get :: /email/{email} requestAuthCode
export async function requestAuthCode(email) {
    const response = await fetch(`${API_URL_AUTH}/email/${email}`,{
        method: 'GET',
    });
    if(!response.ok){
        throw new error('인증 코드 요청 실패');
    }

    return response.json();
}

// Post :: /email validateAuthCode
export async function validateAuthCode(data) {
    const response = await fetch(`${API_URL_AUTH}/email`,{
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(data),
    });

    if(!response.ok){
        throw new Error("이메일 인증 실패")
    }
    return response.json();
}
    

 
// Post :: /login login
export async function login(data) {
    const response = await fetch(`${API_URL_AUTH}/email`,{
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',

        },
        body: JSON.stringify(data),
    });
    return response.json();
    
}

// /api/qna
// Post :: QNA 입력 및 저장
export async function query(data) {
    const response =await fetch(`${API_URL_QNA}`,{
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(data),
    });

    if(!response.ok){
        throw new Error("QnA 게시 실패")
    }
    
    return response.json();
    
}

// Get :: QNA페이지 목록
export async function retrieveQnaPage(qnas) {
    const response = 
            await fetch(API_URL_QNA, {
    method: 'GET',
    cache: 'no-store',
});
if(!response.ok){
        throw new error("QnA 목록 불러오기 실패")
    }
    return response.json();
}

// Get :: /{id}QNA 상세
export async function retrieveQnaDetails(id) {
    const response = 
            await fetch(`${API_URL_QNA}/${id}`, {
    method: 'GET',
    cache: 'no-store',
});
if(!response.ok){
        throw new error("QnA 불러오기 실패")
    }
    return response.json();
}

// Delete :: /{id}  QNA삭제 
export async function deleteQna(id) {
    const response = 
            await fetch(`${API_URL_QNA}/${id}`, {
    method: 'DELETE',
});
    if(!response.ok){
        throw new error("삭제 실패")
    }
    return response.json();
}

