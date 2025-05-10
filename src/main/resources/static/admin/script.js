/**
 * 스터디 그룹 관리 시스템 - 관리자 패널 JavaScript
 */

// 전역 변수
let currentAdminId = null; // 실제 구현시 로그인 정보에서 가져와야 함
const API_BASE_URL = '/api/admin';

// 페이지 로드 시 초기화
document.addEventListener('DOMContentLoaded', function() {
    // 임시로 관리자 ID 설정 (실제 구현에서는 로그인 후 세션에서 가져와야 함)
    currentAdminId = 1;
    
    // 메뉴 탭 이벤트 리스너 등록
    setupMenuListeners();
    
    // 기본 사용자 목록 로드
    loadMembers();
    
    // 새 공지사항 버튼 이벤트 리스너
    document.getElementById('newAnnouncementBtn').addEventListener('click', () => {
        openAnnouncementModal();
    });
    
    // 공지사항 저장 버튼 이벤트 리스너
    document.getElementById('saveAnnouncementBtn').addEventListener('click', saveAnnouncement);
    
    // 로그아웃 버튼 이벤트 리스너
    document.getElementById('logoutBtn').addEventListener('click', function(e) {
        e.preventDefault();
        // 로그아웃 처리 (실제 구현 필요)
        alert('로그아웃 기능은 아직 구현되지 않았습니다.');
    });
});

/**
 * 메뉴 탭 클릭 이벤트 설정
 */
function setupMenuListeners() {
    const menuItems = document.querySelectorAll('.list-group-item');
    
    menuItems.forEach(item => {
        item.addEventListener('click', function(e) {
            e.preventDefault();
            
            // 활성 메뉴 아이템 변경
            menuItems.forEach(mi => mi.classList.remove('active'));
            this.classList.add('active');
            
            // 해당 섹션 표시
            const sectionId = this.getAttribute('data-section');
            showSection(sectionId);
            
            // 섹션에 따라 데이터 로드
            if (sectionId === 'members') {
                loadMembers();
            } else if (sectionId === 'studies') {
                loadStudies();
            } else if (sectionId === 'announcements') {
                loadAnnouncements();
            }
        });
    });
}

/**
 * 특정 섹션을 표시하고 나머지는 숨김
 */
function showSection(sectionId) {
    const sections = document.querySelectorAll('.content-section');
    
    sections.forEach(section => {
        if (section.id === `${sectionId}-section`) {
            section.classList.remove('d-none');
        } else {
            section.classList.add('d-none');
        }
    });
}

/**
 * 모든 사용자 정보 로드
 */
function loadMembers() {
    fetch(`${API_BASE_URL}/users?adminId=${currentAdminId}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('권한이 없거나 사용자 정보를 가져오는데 실패했습니다.');
            }
            return response.json();
        })
        .then(data => {
            if (data && data.data) {
                displayMembers(data.data);
            }
        })
        .catch(error => {
            console.error('사용자 목록 로드 중 오류 발생:', error);
            alert('사용자 목록을 불러오는데 실패했습니다: ' + error.message);
        });
}

/**
 * 사용자 목록 표시
 */
function displayMembers(members) {
    const tableBody = document.getElementById('membersTableBody');
    tableBody.innerHTML = '';
    
    if (members.length === 0) {
        tableBody.innerHTML = '<tr><td colspan="6" class="text-center">등록된 사용자가 없습니다.</td></tr>';
        return;
    }
    
    members.forEach(member => {
        const row = document.createElement('tr');
        
        row.innerHTML = `
            <td>${member.id}</td>
            <td>${member.userName}</td>
            <td>${member.email}</td>
            <td>${formatDateTime(member.createAt)}</td>
            <td>${formatDateTime(member.modifiedAt)}</td>
            <td>
                <button class="btn btn-sm btn-primary" onclick="editMember(${member.id}, '${member.userName}', '${member.email}')">
                    <i class="bi bi-pencil"></i> 수정
                </button>
            </td>
        `;
        
        tableBody.appendChild(row);
    });
}

/**
 * 모든 스터디 로드
 */
function loadStudies() {
    // 여기서는 스터디 목록을 가져오는 API가 직접적으로 없어서 간단히 구현
    // 실제로는 적절한 API를 호출해야 함
    
    const dummyStudies = []; // 샘플 데이터 또는 실제 API 호출로 대체
    
    fetch('/api/studies') // 가정된 API 경로
        .then(response => {
            if (!response.ok) {
                throw new Error('스터디 목록을 가져오는데 실패했습니다');
            }
            return response.json();
        })
        .then(data => {
            displayStudies(data || dummyStudies);
        })
        .catch(error => {
            console.error('스터디 목록 로드 중 오류 발생:', error);
            // 에러 시에도 UI를 표시하기 위해 빈 배열 전달
            displayStudies([]);
        });
}

/**
 * 스터디 목록 표시
 */
function displayStudies(studies) {
    const tableBody = document.getElementById('studiesTableBody');
    tableBody.innerHTML = '';
    
    if (studies.length === 0) {
        tableBody.innerHTML = '<tr><td colspan="7" class="text-center">등록된 스터디가 없습니다.</td></tr>';
        return;
    }
    
    studies.forEach(study => {
        const row = document.createElement('tr');
        
        row.innerHTML = `
            <td>${study.id}</td>
            <td>${study.name}</td>
            <td>${truncateText(study.description, 50)}</td>
            <td>${study.leader ? study.leader.userName : 'N/A'}</td>
            <td>${formatDateTime(study.createAt)}</td>
            <td>${formatDateTime(study.modifiedAt)}</td>
            <td>
                <button class="btn btn-sm btn-danger" onclick="confirmDeleteStudy(${study.id})">
                    <i class="bi bi-trash"></i> 삭제
                </button>
            </td>
        `;
        
        tableBody.appendChild(row);
    });
}

/**
 * 삭제 전 확인 모달 표시
 */
function confirmDeleteStudy(studyId) {
    document.getElementById('deleteConfirmMessage').textContent = `정말로 스터디 그룹 (ID: ${studyId})을 삭제하시겠습니까?`;
    
    // 삭제 확인 버튼에 이벤트 리스너 설정
    const confirmBtn = document.getElementById('confirmDeleteBtn');
    confirmBtn.onclick = function() {
        deleteStudy(studyId);
    };
    
    // 모달 표시
    const modal = new bootstrap.Modal(document.getElementById('deleteConfirmModal'));
    modal.show();
}

/**
 * 스터디 삭제 실행
 */
function deleteStudy(studyId) {
    fetch(`${API_BASE_URL}/studies/${studyId}?adminId=${currentAdminId}`, {
        method: 'DELETE'
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('스터디 삭제 중 오류가 발생했습니다.');
        }
        return response.json();
    })
    .then(data => {
        // 성공 메시지 표시
        alert('스터디가 성공적으로 삭제되었습니다.');
        
        // 모달 닫기
        const modal = bootstrap.Modal.getInstance(document.getElementById('deleteConfirmModal'));
        modal.hide();
        
        // 스터디 목록 다시 로드
        loadStudies();
    })
    .catch(error => {
        console.error('스터디 삭제 중 오류 발생:', error);
        alert('스터디 삭제에 실패했습니다: ' + error.message);
    });
}

/**
 * 공지사항 목록 로드
 */
function loadAnnouncements() {
    fetch(`${API_BASE_URL}/announcements?adminId=${currentAdminId}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('공지사항 목록을 가져오는데 실패했습니다.');
            }
            return response.json();
        })
        .then(data => {
            if (data && data.data) {
                displayAnnouncements(data.data);
            }
        })
        .catch(error => {
            console.error('공지사항 목록 로드 중 오류 발생:', error);
            alert('공지사항 목록을 불러오는데 실패했습니다: ' + error.message);
            // 에러 시에도 UI를 표시하기 위해 빈 배열 전달
            displayAnnouncements([]);
        });
}

/**
 * 공지사항 목록 표시
 */
function displayAnnouncements(announcements) {
    const tableBody = document.getElementById('announcementsTableBody');
    tableBody.innerHTML = '';
    
    if (announcements.length === 0) {
        tableBody.innerHTML = '<tr><td colspan="7" class="text-center">등록된 공지사항이 없습니다.</td></tr>';
        return;
    }
    
    announcements.forEach(announcement => {
        const row = document.createElement('tr');
        if (announcement.important) {
            row.classList.add('important-announcement');
        }
        
        row.innerHTML = `
            <td>${announcement.id}</td>
            <td>${announcement.title}</td>
            <td>${announcement.author ? announcement.author.userName : 'N/A'}</td>
            <td>${formatDateTime(announcement.publishDate)}</td>
            <td>${announcement.expiryDate ? formatDateTime(announcement.expiryDate) : '없음'}</td>
            <td>${announcement.important ? '<span class="badge bg-warning">중요</span>' : '일반'}</td>
            <td>
                <button class="btn btn-sm btn-info me-1" onclick="editAnnouncement(${announcement.id})">
                    <i class="bi bi-pencil"></i>
                </button>
                <button class="btn btn-sm btn-danger" onclick="confirmDeleteAnnouncement(${announcement.id})">
                    <i class="bi bi-trash"></i>
                </button>
            </td>
        `;
        
        tableBody.appendChild(row);
    });
}

/**
 * 공지사항 모달 열기 (새 공지사항 또는 편집)
 */
function openAnnouncementModal(announcementId = null) {
    const modal = new bootstrap.Modal(document.getElementById('announcementModal'));
    const modalTitle = document.getElementById('announcementModalTitle');
    const form = document.getElementById('announcementForm');
    
    // 폼 초기화
    form.reset();
    document.getElementById('announcementId').value = '';
    
    if (announcementId) {
        // 기존 공지사항 수정 모드
        modalTitle.textContent = '공지사항 수정';
        
        // 기존 공지사항 데이터 로드
        fetch(`${API_BASE_URL}/announcements/${announcementId}?adminId=${currentAdminId}`)
            .then(response => response.json())
            .then(data => {
                if (data && data.data) {
                    const announcement = data.data;
                    document.getElementById('announcementId').value = announcement.id;
                    document.getElementById('announcementTitle').value = announcement.title;
                    document.getElementById('announcementContent').value = announcement.content;
                    
                    if (announcement.expiryDate) {
                        document.getElementById('expiryDate').value = formatDateForInput(announcement.expiryDate);
                    }
                    
                    document.getElementById('importantCheck').checked = announcement.important;
                }
                modal.show();
            })
            .catch(error => {
                console.error('공지사항 로드 중 오류 발생:', error);
                alert('공지사항 정보를 불러오는데 실패했습니다.');
                modal.show();
            });
    } else {
        // 새 공지사항 모드
        modalTitle.textContent = '새 공지사항';
        modal.show();
    }
}

/**
 * 공지사항 수정
 */
function editAnnouncement(announcementId) {
    openAnnouncementModal(announcementId);
}

/**
 * 공지사항 삭제 확인
 */
function confirmDeleteAnnouncement(announcementId) {
    document.getElementById('deleteConfirmMessage').textContent = `정말로 이 공지사항을 삭제하시겠습니까?`;
    
    // 삭제 확인 버튼에 이벤트 리스너 설정
    const confirmBtn = document.getElementById('confirmDeleteBtn');
    confirmBtn.onclick = function() {
        deleteAnnouncement(announcementId);
    };
    
    // 모달 표시
    const modal = new bootstrap.Modal(document.getElementById('deleteConfirmModal'));
    modal.show();
}

/**
 * 회원 수정 함수 - 회원 수정 모달 표시
 */
function editMember(memberId, userName, email) {
    // 모달 창 생성 혹은 존재하지 않으면 추가
    let modal = document.getElementById('memberModal');
    
    if (!modal) {
        // 모달이 없으면 동적으로 생성
        const modalHTML = `
            <div class="modal fade" id="memberModal" tabindex="-1">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header bg-primary text-white">
                            <h5 class="modal-title" id="memberModalTitle">회원 정보 수정</h5>
                            <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <form id="memberForm">
                                <input type="hidden" id="memberId">
                                <div class="mb-3">
                                    <label for="memberUserName" class="form-label">사용자명</label>
                                    <input type="text" class="form-control" id="memberUserName" required>
                                </div>
                                <div class="mb-3">
                                    <label for="memberEmail" class="form-label">이메일</label>
                                    <input type="email" class="form-control" id="memberEmail" required>
                                </div>
                            </form>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">취소</button>
                            <button type="button" class="btn btn-primary" id="saveMemberBtn">저장</button>
                        </div>
                    </div>
                </div>
            </div>
        `;
        
        // body에 모달 HTML 추가
        document.body.insertAdjacentHTML('beforeend', modalHTML);
        
        // 저장 버튼에 이벤트 리스너 추가
        document.getElementById('saveMemberBtn').addEventListener('click', saveMember);
        
        // 모달 엘리먼트 참조 가져오기
        modal = document.getElementById('memberModal');
    }
    
    // 모달에 회원 정보 설정
    document.getElementById('memberId').value = memberId;
    document.getElementById('memberUserName').value = userName;
    document.getElementById('memberEmail').value = email;
    
    // 모달 표시
    const bsModal = new bootstrap.Modal(modal);
    bsModal.show();
}

/**
 * 회원 정보 저장 함수
 */
function saveMember() {
    const memberId = document.getElementById('memberId').value;
    const userName = document.getElementById('memberUserName').value;
    const email = document.getElementById('memberEmail').value;
    
    // 유효성 검사
    if (!userName || !email) {
        alert('사용자명과 이메일은 필수 입력 항목입니다.');
        return;
    }
    
    // 요청 데이터 준비
    const requestData = {
        userName: userName,
        email: email
    };
    
    // API 호출
    fetch(`${API_BASE_URL}/users/${memberId}?adminId=${currentAdminId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(requestData)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('회원 정보 수정 중 오류가 발생했습니다.');
        }
        return response.json();
    })
    .then(data => {
        // 성공 메시지 표시
        alert('회원 정보가 성공적으로 수정되었습니다.');
        
        // 모달 닫기
        const modal = bootstrap.Modal.getInstance(document.getElementById('memberModal'));
        modal.hide();
        
        // 회원 목록 다시 로드
        loadMembers();
    })
    .catch(error => {
        console.error('회원 정보 수정 중 오류 발생:', error);
        alert('회원 정보 수정에 실패했습니다: ' + error.message);
    });
}

/**
 * 공지사항 삭제
 */
function deleteAnnouncement(announcementId) {
    fetch(`${API_BASE_URL}/announcements/${announcementId}?adminId=${currentAdminId}`, {
        method: 'DELETE'
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('공지사항 삭제 중 오류가 발생했습니다.');
        }
        return response.json();
    })
    .then(data => {
        // 성공 메시지 표시
        alert('공지사항이 성공적으로 삭제되었습니다.');
        
        // 모달 닫기
        const modal = bootstrap.Modal.getInstance(document.getElementById('deleteConfirmModal'));
        modal.hide();
        
        // 공지사항 목록 다시 로드
        loadAnnouncements();
    })
    .catch(error => {
        console.error('공지사항 삭제 중 오류 발생:', error);
        alert('공지사항 삭제에 실패했습니다: ' + error.message);
    });
}

/**
 * 공지사항 저장 (새로 작성 또는 수정)
 */
function saveAnnouncement() {
    const announcementId = document.getElementById('announcementId').value;
    const title = document.getElementById('announcementTitle').value;
    const content = document.getElementById('announcementContent').value;
    const expiryDateInput = document.getElementById('expiryDate').value;
    const important = document.getElementById('importantCheck').checked;
    
    // 유효성 검사
    if (!title || !content) {
        alert('제목과 내용은 필수 입력 항목입니다.');
        return;
    }
    
    // 요청 데이터 준비
    const requestData = {
        title: title,
        content: content,
        important: important
    };
    
    // 만료일이 입력되었으면 추가
    if (expiryDateInput) {
        requestData.expiryDate = new Date(expiryDateInput + 'T23:59:59').toISOString();
    }
    
    const isNewAnnouncement = !announcementId;
    const url = isNewAnnouncement 
        ? `${API_BASE_URL}/announcements?adminId=${currentAdminId}`
        : `${API_BASE_URL}/announcements/${announcementId}?adminId=${currentAdminId}`;
    
    fetch(url, {
        method: isNewAnnouncement ? 'POST' : 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(requestData)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('공지사항 저장 중 오류가 발생했습니다.');
        }
        return response.json();
    })
    .then(data => {
        // 성공 메시지 표시
        alert(`공지사항이 성공적으로 ${isNewAnnouncement ? '생성' : '수정'}되었습니다.`);
        
        // 모달 닫기
        const modal = bootstrap.Modal.getInstance(document.getElementById('announcementModal'));
        modal.hide();
        
        // 공지사항 목록 다시 로드
        loadAnnouncements();
    })
    .catch(error => {
        console.error('공지사항 저장 중 오류 발생:', error);
        alert('공지사항 저장에 실패했습니다: ' + error.message);
    });
}

/**
 * 유틸리티 함수: 날짜 형식화
 */
function formatDateTime(dateTimeStr) {
    if (!dateTimeStr) return 'N/A';
    
    const date = new Date(dateTimeStr);
    if (isNaN(date.getTime())) return 'Invalid Date';
    
    return date.toLocaleString('ko-KR', { 
        year: 'numeric', 
        month: '2-digit', 
        day: '2-digit',
        hour: '2-digit', 
        minute: '2-digit'
    });
}

/**
 * 유틸리티 함수: 날짜 입력용 형식 변환 (YYYY-MM-DD)
 */
function formatDateForInput(dateTimeStr) {
    if (!dateTimeStr) return '';
    
    const date = new Date(dateTimeStr);
    if (isNaN(date.getTime())) return '';
    
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    
    return `${year}-${month}-${day}`;
}

/**
 * 유틸리티 함수: 텍스트 길이 제한
 */
function truncateText(text, maxLength) {
    if (!text) return '';
    if (text.length <= maxLength) return text;
    return text.substr(0, maxLength) + '...';
}
