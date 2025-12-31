'use client'


import { useEffect, useState } from "react";
import AnswerComments from "./component/AnsweComment";
import AddComment from "./component/CreateComment";
import { retrieveQna } from "@/app/api/qna";



export default function QnaDetail({params}) {
    const {qnaId} = params
    
    const [qna, setQna] = useState(null);
    const [loading, setLoading] = useState(true);
    

    const loadQna = async () => {
        const data = await retrieveQna(qnaId);
        setQna(data)
    }

    useEffect(() => {
        (async () => {
            try {
                setLoading(ture)
                await loadQna();
            }catch(e) {
                alert(e.message);
            }finally{
                setLoading(false);
            }
        })();
    }, [qnaId]) 

    const hasAnswers = Array.isArray(qna?.comments) && qna.comments.length > 0;


    if (loading) return <div>읽어오는 중</div>
    if (!qna) return <div>존재하지 않은 게시물입니다.</div>
   

    return (
        <div>
            <h1>제목 : {qna.title}</h1>
            <p>내용 : {qna.content}</p>
            <div>작성자 : {qna.username}</div>

            {hasAnswers ? <AnswerComments comments={qna?.comments}/> :
                <AddComment
                    qnaId={qnaId} 
                    onSuccess={loadQna}
                />
            }
            
        </div>
        

    )
    
}