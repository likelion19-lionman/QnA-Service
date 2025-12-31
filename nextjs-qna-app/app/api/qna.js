import { baseRequest } from './api';

export async function query(title, comment) {
    return await baseRequest(
        '/qna',
        'POST',
        {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        },
        {
            title: title,
            comment: comment
        },
        `QnA 생성 실패`
    )
}

export async function retrieveQnas(page, size) {
    return await baseRequest(
        `/qna?page=${page}&size=${size}`,
        'GET',
        {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        },
        {},
        `조회 실패`
    )
}

export async function retrieveQna(qnaId) {
    return await baseRequest(
        `/qna?id=${qnaId}`,
        'GET',
        {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        },
        {},
        `${qnaId} 게시물 조회 실패`
    )
}

export async function addComment(qnaId, comment) {
    return await baseRequest(
        `/qna/${qnaId}`,
        'POST',
        {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        },
        comment,
        `${qnaId} 에 댓글 작성을 실패했습니다.`
    )
}

export async function deleteQna(qnaId) {
    await baseRequest(
        `/qna`,
        'DELETE',
        {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        },
        {},
        `${qnaId} 삭제에 실패했습니다.`
    )
}