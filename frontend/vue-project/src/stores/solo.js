
import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import { useRouter } from 'vue-router';
import axios from 'axios';


export const useSoloStore = defineStore('solo', () => {
    const URL = 'http://localhost:8080/solo';
    // 솔로 모드 내역(오늘) 데이터 저장
    // api 연결되면 지우자.
    const soloTodayData = ref([
        {
            solo_status: 1,
            solo_result: 1,
            category_id: 1,
        },
        {
            solo_status: 0,
            solo_result: 1,
            category_id: 2,
        },
        {
            solo_status: 1,
            solo_result: 1,
            category_id: 3,
        },
        {
            solo_status: 1,
            solo_result: 1,
            category_id: 4,
        },
        {
            solo_status: 0,
            solo_result: 1,
            category_id: 5,
        },
        {
            solo_status: 0,
            solo_result: 1,
            category_id: 6,
        },
    ])

    // 솔로 모드 내역 표시(오늘) 함수
    const soloToday = function (userId) {
        return new Promise((resolve, reject) => {
            axios
                .get(`${URL}/today-list/${userId}`)
                .then((res) => {

                    const formattedData = {};
                    res.data.forEach(item => {
                        formattedData[item.solo_id] = {
                            solo_status: item.solo_status,
                            solo_result: item.solo_result,
                            category_id: item.category_id
                        };
                    });

                    soloData.value = formattedData;

                    resolve(formattedData);
                })
                .catch((err) => {
                    console.log(err);
                    reject(err);
                });
        });
    }

    //솔로모드 도전
    const soloChallenge = function (challenge) {
        const category = { user_id: challenge.user_id, category_id: challenge.category_id }
        return new Promise((resolve, reject) => {
            axios
                .post(`${URL}/${category.category_id}`, category)
                .then((res) => {
                    resolve(res)
                })
                .catch((err) => {
                    console.log(err);
                    reject(err)
                })
        })
    }

    //솔로모드 인증
    const soloAuth = function (soloauth) {

        const solo_auth = { solo_id: soloauth.id, solo_img: soloauth.img }

        return new Promise((resolve, reject) => {
            axios
                .patch(`${URL}/${solo_auth.solo_id}/challenge-auth`, solo_auth.solo_img)
                .then((res) => {
                    resolve(res)
                    console.log('업로드 완료')
                })
                .catch((err) => {
                    reject(err)
                })
        })
    }
    return { soloTodayData, soloToday, soloChallenge, soloAuth }
})



