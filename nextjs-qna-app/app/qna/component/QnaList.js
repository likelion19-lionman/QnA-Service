'use client'

import  {retrieveQnas}  from "@/app/api/qna";
import { useEffect, useState } from "react";

export default function Qnas() {
    const [qnas, setQnas] = useState([]);
    const [page, setPage] = useState(0);

    useEffect(() => {
        (async () => {
            try{
                const data = await retrieveQnas(page, 10);
                setQnas(data)
            }catch(e) {
                alert(e.message)
            }
        })();
    }, [page]);

    return (
        <div>
            <h1>QnA List</h1>
            <ul>
                {qnas.map((qna) => (
                    <li key={qna.id}>
                        <Link href={`/qna/${qna.id}`}>
                            {qna.title}
                        </Link>
                    </li>
                ))}
            </ul>

            <button
                type="button"
                onClick={()=>setPage((p) => Math.max(0,p-1))}>
                이전
            </button>
            <button
                type="button"
                onClick={()=>setPage((p) => p+1)}>
                다음
            </button>
        </div>
    )


}