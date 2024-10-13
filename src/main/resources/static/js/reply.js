async function get1(bno){ /*매개변수로 받은 bno에 등록된 댓글들을 반환*/

    const result = await axios.get(`/replies/list/${bno}`)

    // console.log(result);

    return result.data
}
/*async : 비동기 선언, await : async내부 비동기 호출부분에 사용*/

async function getList({bno, page, size, goLast}){

    const result = await axios.get(`/replies/list/${bno}`, {params : {page, size}})

    if (goLast){
        const total = result.data.total
        const lastPage = parseInt(Math.ceil(total / size))
        return getList({bno: bno, page: lastPage, size: size})
    }

    return result.data
}

async function addReply(replyObj){

    const response = await axios.post(`/replies/`, replyObj)

    return response.data
}

async function getReply(rno){

    const response = await axios.get(`/replies/${rno}`)

    return response.data
}

async function modifyReply(replyObj){

    const response = await axios.put(`/replies/${replyObj.rno}`, replyObj)

    return response.data
}

async function removeReply(rno){

    const response = await axios.delete(`/replies/${rno}`)

    return response.data
}